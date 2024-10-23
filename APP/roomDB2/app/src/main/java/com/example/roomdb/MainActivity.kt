package com.example.roomdb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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
        database = AppDatabase.getDB(this)
        userDao = database.userDao()

        val name = findViewById<EditText>(R.id.editTextText).text.toString()
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
        val user = User(name = name, email = email)
        //userDao.insert(user)
        val addbtn = findViewById<Button>(R.id.btn_add)
        val findbtn = findViewById<Button>(R.id.btn_find)
        val result = findViewById<TextView>(R.id.TV_result)

        addbtn.setOnClickListener {
            val name = findViewById<EditText>(R.id.editTextText).text.toString()
            val email = findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val user = User(name = name, email = email)
            lifecycleScope.launch {
                userDao.insert(user)
                val users = userDao.getAll()
                userDao.insert(user)
                result.text = "新增成功"
            }
        }
        findbtn.setOnClickListener {
            lifecycleScope.launch {
                val users = userDao.getAll()
                result.text = users.joinToString("\n") {"ID:${it.id} Name:${it.name} Email:${it.email}"  }
            }




        }

    }

}