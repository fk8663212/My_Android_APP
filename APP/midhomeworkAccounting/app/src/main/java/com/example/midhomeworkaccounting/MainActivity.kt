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
import com.example.midhomeworkaccounting.RecyclerView.TransactionAdapter


import com.whiteelephant.monthpicker.MonthPickerDialog
import kotlinx.coroutines.launch
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao


    private val items: ArrayList<ListItem> = ArrayList()
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

        val adapter = TransactionAdapter(items)
        //val adapter = RecordAdapter(items)

        rv_result.adapter = adapter
        rv_result.layoutManager = LinearLayoutManager(this)

        val btn_month = findViewById<Button>(R.id.btn_month)
        val btn_add = findViewById<Button>(R.id.btn_del)


//        val today=Calendar.getInstance()
//        btn_month.text="${today.get(Calendar.YEAR)}/${today.get(Calendar.MONTH)+1}"

        // 加載資料並顯示
        loadData()

        // 點擊 btn_add 的監聽器
        btn_add.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent, 1)
        }

        btn_month.setOnClickListener {
            showMonthPicker()
        }
    }

    //當MainActivity2,3關閉後，更新rv_result
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult called: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == 1 || requestCode == 2) {
            if(resultCode == RESULT_OK){
                //loadData()
                loadAndFilterData()
            }

        }
    }
    private fun loadAndFilterData() {
        val btnMonth = findViewById<Button>(R.id.btn_month)
        val selectedMonthYear = btnMonth.text.toString().split("/")

        if (selectedMonthYear.size == 2) {
            val year = selectedMonthYear[0].toIntOrNull()
            val month = selectedMonthYear[1].toIntOrNull()
            if (year != null && month != null) {
                filterDataByMonth(year, month)
                return
            }
        }

        // 如果未選擇月份或格式錯誤，則加載所有數據
        loadData()

    }

    private fun loadData() {
        val tv_money = findViewById<TextView>(R.id.TV_money)
        val rv_result = findViewById<RecyclerView>(R.id.RV_result)

        lifecycleScope.launch {
            // 清空舊的資料
            items.clear()

            // 從資料庫獲取記錄，並按日期排序
            val records = recordDao.getAll()
                .sortedWith(compareByDescending<Record> { it.year }
                    .thenByDescending { it.month }
                    .thenByDescending { it.day })

            var totalAmount = 0
            var currentDate = ""

            records.forEach { record ->
                // 將日期格式化為 "年/月/日" 字串
                val dateStr = "${record.year}/${record.month}/${record.day}"
                if (dateStr != currentDate) {
                    // 如果是新日期，則加入 DateHeader
                    currentDate = dateStr
                    val dailyTotal = records.filter { it.year == record.year && it.month == record.month && it.day == record.day }
                        .sumOf { if (it.isIncome) it.money else -it.money }
                    items.add(ListItem.DateHeader(currentDate, dailyTotal.toString()))
                }
                // 加入 TransactionItem
                items.add(ListItem.TransactionItem(record.id,record.name, record.money.toString(), record.isIncome,record.year,record.month,record.day))

                // 計算總收入/支出
                totalAmount += if (record.isIncome) record.money else -record.money
            }

            // 更新總金額顯示
            tv_money.text = totalAmount.toString()

            // 通知 Adapter 資料變更
            rv_result.adapter?.notifyDataSetChanged()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


    private fun showMonthPicker(){
        val today = Calendar.getInstance()
        MonthPickerDialog.Builder(this, { selectedMonth, selectedYear ->
            val btnMonth = findViewById<Button>(R.id.btn_month)
            btnMonth.text = "$selectedYear/${selectedMonth + 1}"
            filterDataByMonth(selectedYear, selectedMonth + 1)
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH))
            .setMinYear(1990)
            .setMaxYear(2100)
            .setTitle("選擇月份")
            .build()
            .show()
    }
    private fun filterDataByMonth(year: Int, month: Int) {
        lifecycleScope.launch {
            // 清空舊的資料
            items.clear()

            // 從資料庫取得符合條件的資料並進行排序
            val filteredRecords = recordDao.getByMonth(year, month)
                .sortedWith(compareByDescending<Record> { it.year }
                    .thenByDescending { it.month }
                    .thenByDescending { it.day })

            // 創建 ListItem 的清單
            val groupedItems = ArrayList<ListItem>()
            var totalAmount = 0
            var currentDate = ""

            filteredRecords.forEach { record ->
                // 將日期格式化為 "年/月/日" 字串
                val dateStr = "${record.year}/${record.month}/${record.day}"
                if (dateStr != currentDate) {
                    // 如果是新日期，則加入 DateHeader
                    currentDate = dateStr
                    val dailyTotal = filteredRecords.filter { it.year == record.year && it.month == record.month && it.day == record.day }
                        .sumOf { if (it.isIncome) it.money else -it.money }
                    groupedItems.add(ListItem.DateHeader(currentDate, dailyTotal.toString()))
                }
                // 加入 TransactionItem
                groupedItems.add(ListItem.TransactionItem(record.id,record.name, record.money.toString(), record.isIncome,record.year,record.month,record.day))

                // 計算當月總收入/支出
                totalAmount += if (record.isIncome) record.money else -record.money
            }

            // 更新顯示總金額
            findViewById<TextView>(R.id.TV_money).text = totalAmount.toString()

            // 更新 RecyclerView 的 Adapter 資料
            val rv_result = findViewById<RecyclerView>(R.id.RV_result)
            rv_result.adapter = TransactionAdapter(groupedItems)
            rv_result.adapter?.notifyDataSetChanged()
        }
    }



}

