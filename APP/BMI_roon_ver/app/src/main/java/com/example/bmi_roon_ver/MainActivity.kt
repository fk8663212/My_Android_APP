package com.example.bmi_roon_ver

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
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
        userDao = database.userDao()

        val nameInput = findViewById<EditText>(R.id.ET_name)
        val heightInput = findViewById<EditText>(R.id.ET_height)
        val weightInput = findViewById<EditText>(R.id.ET_weight)

        val addButton = findViewById<Button>(R.id.btn_add)
        val modButton = findViewById<Button>(R.id.btn_mod)
        val delButton = findViewById<Button>(R.id.btn_del)
        val findButton = findViewById<Button>(R.id.btn_find)
        var TVresult = findViewById<TextView>(R.id.TV_result)

        //設置按鈕事件
        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val height = heightInput.text.toString().toInt()
            val weight = weightInput.text.toString().toInt()
            val bmi = weight / (height / 100.0).pow(2.0)
            val user = User(name = name, height = height, weight = weight, bmi = bmi)

            lifecycleScope.launch {
                userDao.insertUser(user)
                showToast("新增成功")
            }
            findButton.setOnClickListener {
                lifecycleScope.launch {
                    val users = userDao.getAllUsers()
                    val result = users.joinToString("\n") { "${it.name} ${it.height} ${it.weight} ${it.bmi}" }
                    TVresult.text = result
                    showToast("查詢成功")
                }
            }
        }







    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}