package com.example.midhomeworkaccounting

import android.content.Intent
import android.icu.text.AlphabeticIndex
import android.os.Bundle
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

class MainActivity2 : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = AppDatabase.getDatabase(this)
        recordDao = database.recordDao()

        val btn_add = findViewById<Button>(R.id.btn_add)
        val ET_name = findViewById<EditText>(R.id.ET_name)
        val ET_money = findViewById<EditText>(R.id.ET_money)
        val SW_income = findViewById<Switch>(R.id.SW_income)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        btn_add.setOnClickListener {
            val name = ET_name.text.toString()
            val money = ET_money.text.toString().toInt()
            val isIncome = SW_income.isChecked
            val date = calendarView.date.toString()
            val record = Record(name = name, money = money, isIncome = isIncome, date = date)
            lifecycleScope.launch {
                recordDao.insert(record)
                showToast("新增成功")
            }
            val intent = Intent("com.example.midhomeworkaccounting.UPDATE_RV")
            sendBroadcast(intent)


            finish()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}