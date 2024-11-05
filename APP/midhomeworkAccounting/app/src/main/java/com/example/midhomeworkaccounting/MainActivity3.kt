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
        var cyear = 0
        var cmonth = 0
        var cday = 0

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date = "$year/${month + 1}/$dayOfMonth"
            showToast(date)
            cyear = year
            cmonth = month
            cday = dayOfMonth

        }


        val position = intent.getIntExtra("position", -1)

        // 獲取記錄
        if (position != -1) {
            loadRecord(position)
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

    private fun loadRecord(position: Int) {
        lifecycleScope.launch {
            // 獲取並顯示記錄
            record = recordDao.getAll()[position]
            record?.let {
                findViewById<EditText>(R.id.ET_name).setText(it.name)
                findViewById<EditText>(R.id.ET_money).setText(it.money.toString())
                findViewById<Switch>(R.id.SW_income).isChecked = it.isIncome
                // 這裡可以根據需求設定 CalendarView 的日期
            }
        }
    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}