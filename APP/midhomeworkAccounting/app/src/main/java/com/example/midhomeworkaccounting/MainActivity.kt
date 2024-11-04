package com.example.midhomeworkaccounting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import kotlinx.coroutines.launch

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

        val btn_add = findViewById<Button>(R.id.btn_del)
        // 加載資料並顯示
        loadData()

        btn_add.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent, 1)
        }




    }

    //當MainActivity2關閉後，更新rv_result
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val tv_money = findViewById<TextView>(R.id.TV_money)
        val rv_result = findViewById<RecyclerView>(R.id.RV_result)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
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
        holder.textView.text = record.name
        holder.textView2.text = record.money.toString()

        if (record.isIncome){
            holder.textView2.text = "+"+record.money.toString()
            holder.textView2.setTextColor(Color.GREEN)
        }
        else{
            holder.textView2.text = "-"+record.money.toString()
            holder.textView2.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

