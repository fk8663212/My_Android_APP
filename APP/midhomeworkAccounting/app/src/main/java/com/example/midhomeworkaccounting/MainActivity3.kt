package com.example.midhomeworkaccounting

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity3 : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao

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


        btn_del.setOnClickListener {
            val name = ET_name.text.toString()
            val money = ET_money.text.toString().toInt()
            val isIncome = SW_income.isChecked
            val date = calendarView.date.toString()
            val record = Record(name = name, money = money, isIncome = isIncome, date = date)
            finish()

        }


    }
}