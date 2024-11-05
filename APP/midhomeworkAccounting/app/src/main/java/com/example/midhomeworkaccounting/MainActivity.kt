package com.example.midhomeworkaccounting

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao


    private val items: ArrayList<Record> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDatabase.getDatabase(this)
        recordDao = database.recordDao()

        // 初始化 RecyclerView 和 Adapter
        val rv_result = findViewById<RecyclerView>(R.id.RV_result)

        val adapter = RecordAdapter(items)
        rv_result.adapter = adapter
        rv_result.layoutManager = LinearLayoutManager(this)

        val btn_month = findViewById<Button>(R.id.btn_month)
        val btn_add = findViewById<Button>(R.id.btn_del)



        // 加載資料並顯示
        loadData()

        // 點擊 btn_add 的監聽器
        btn_add.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent, 1)
        }

        btn_month.setOnClickListener {
            showMonthPicker2()
        }
    }

    //當MainActivity2,3關閉後，更新rv_result
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult called: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == 1 || requestCode == 2) {
            if(resultCode == RESULT_OK)
                loadData()
        }
    }
    private fun loadData() {
        val tv_money = findViewById<TextView>(R.id.TV_money)
        val rv_result = findViewById<RecyclerView>(R.id.RV_result)
        lifecycleScope.launch {
            items.clear()
            val c = recordDao.getCount()
            showToast("總共 $c 筆資料")
            val records = recordDao.getAll()
            items.addAll(records)

            var total = 0
            records.forEach {
                if (it.isIncome) {
                    total += it.money
                } else {
                    total -= it.money
                }
            }
            tv_money.text = total.toString()

            // 通知 Adapter 資料變更
            rv_result.adapter?.notifyDataSetChanged()
        }
    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun showMonthPicker(){
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("選擇月份")
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection ?: 0L
            val selectedYear = calendar.get(Calendar.YEAR)
            val selectedMonth = calendar.get(Calendar.MONTH) + 1 // 月份從0開始，所以加1

            val btn_month = findViewById<Button>(R.id.btn_month)
            btn_month.text = "$selectedYear/$selectedMonth"

            // 根據選擇的月份和年份進行資料過濾
        }

        datePicker.show(supportFragmentManager, "MonthPicker")

    }
    private fun showMonthPicker2(){
        val today = Calendar.getInstance()

        MonthPickerDialog.Builder(this, { selectedMonth, selectedYear ->
            val btnMonth = findViewById<Button>(R.id.btn_month)
            btnMonth.text = "$selectedYear/${selectedMonth + 1}"
            //filterDataByMonth(selectedYear, selectedMonth + 1)
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH))
            .setMinYear(1990)
            .setMaxYear(2100)
            .setTitle("選擇月份")
            .build()
            .show()

    }
//    private fun showMonthYearPicker() {
//        val calendar = Calendar.getInstance()
//        val datePickerDialog = DatePickerDialog(
//            this,
//            { _, year, month, _ ->
//                val btnMonth = findViewById<Button>(R.id.btn_month)
//                btnMonth.text = "$year/${month + 1}"
//                //filterDataByMonth(year, month + 1)
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//
//        // 隱藏日期選擇部分
//        datePickerDialog.datePicker.findViewById<View>(
//            resources.getIdentifier("android:id/day", null, null)
//        )?.visibility = View.GONE
//
//        datePickerDialog.show()
//    }



}


class RecordAdapter(private val items: List<Record>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_description)
        val textView2: TextView = itemView.findViewById(R.id.tv_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = items[position]
        holder.textView.text = record.date
        holder.textView2.text = record.money.toString()

        if (record.isIncome){
            holder.textView2.text = "+"+record.money.toString()
            holder.textView2.setTextColor(Color.GREEN)
        }
        else{
            holder.textView2.text = "-"+record.money.toString()
            holder.textView2.setTextColor(Color.RED)
        }
        //點擊事件切換至另一個activity3
        //傳送資料位置，以便在activity3進行刪除修改
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MainActivity3::class.java)
            intent.putExtra("position", position)
            (holder.itemView.context as? AppCompatActivity)?.startActivityForResult(intent, 2)

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}
