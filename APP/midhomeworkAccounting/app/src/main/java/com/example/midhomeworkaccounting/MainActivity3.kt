package com.example.midhomeworkaccounting

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity3 : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao
    private var record: Record? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDatabase.getDatabase(this)
        recordDao = database.recordDao()

        val btn_del = findViewById<Button>(R.id.btn_del)
        val btn_mod = findViewById<Button>(R.id.btn_mod)
        val ET_name = findViewById<EditText>(R.id.ET_name)
        val ET_money = findViewById<EditText>(R.id.ET_money)
        val SW_income = findViewById<Switch>(R.id.SW_income)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        var date =""
        var cyear = intent.getIntExtra("year", Calendar.getInstance().get(Calendar.YEAR))
        var cmonth = intent.getIntExtra("month", Calendar.getInstance().get(Calendar.MONTH))+1
        var cday = intent.getIntExtra("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))

        val calendar = Calendar.getInstance()
        calendar.set(cyear, cmonth-1, cday)
        calendarView.date = calendar.timeInMillis
        showToast("$cyear/${cmonth}/$cday")

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            cyear = year
            cmonth = month+1
            cday = dayOfMonth
            date = "$cyear/${cmonth}/$cday"
            showToast(date)

        }


        val recordId = intent.getIntExtra("record_id", -1) // 获取record_id
        if (recordId != -1) {
            loadRecord(recordId)
        }

        btn_del.setOnClickListener {
            record?.let {
                lifecycleScope.launch {
                    recordDao.delete(it.id)  // 直接刪除記錄物件
                    showToast("刪除成功")
                    setResult(RESULT_OK, intent)  // 通知 MainActivity 更新
                    Log.d("MainActivity3", "Deleted record: ${it.name}")
                    finish()

                }
            }
        }

        btn_mod.setOnClickListener {
            record?.let {
                lifecycleScope.launch {
                    // 更新記錄
                    recordDao.update(it.id, ET_name.text.toString(), ET_money.text.toString().toInt(),cyear,cmonth,cday, SW_income.isChecked)
                    setResult(RESULT_OK, intent)  // 通知 MainActivity 更新
                    Log.d("MainActivity3", "Updated record: ${it.name}")
                    finish()
                }
            }
        }
    }

    private fun loadRecord(recordId: Int) {
        lifecycleScope.launch {
            // 通过ID而非position查询数据库
            record = recordDao.getRecordById(recordId)
            record?.let {
                findViewById<EditText>(R.id.ET_name).setText(it.name)
                findViewById<EditText>(R.id.ET_money).setText(it.money.toString())
                findViewById<Switch>(R.id.SW_income).isChecked = it.isIncome
                // 可以设置 CalendarView 的日期
            }
        }
    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}