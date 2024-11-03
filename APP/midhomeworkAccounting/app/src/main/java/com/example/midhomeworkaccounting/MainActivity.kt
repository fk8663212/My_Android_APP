package com.example.midhomeworkaccounting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var recordDao: RecordDao

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

        val btn_add = findViewById<Button>(R.id.btn_add)
        val btn_add2 = findViewById<Button>(R.id.btn_add2)
        val rv_result = findViewById<RecyclerView>(R.id.RV_result)
        val tv_money = findViewById<TextView>(R.id.TV_money)
        //當MainActivity2關閉後，更新rv_result

        val intentFilter = IntentFilter().apply {
            addAction("com.example.midhomeworkaccounting.UPDATE_RV")
        }












        btn_add.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        btn_add2.setOnClickListener {
        lifecycleScope.launch {
            val records = recordDao.getAll()
            val result = records.joinToString("\n") {"${it.name} ${it.money}"}
            tv_money.text = result
        }}
    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}