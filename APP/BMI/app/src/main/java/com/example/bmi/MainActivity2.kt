package com.example.bmi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val textView = findViewById<TextView>(R.id.TV_result)
        val name = intent.getStringExtra("name")
        val weight = intent.getFloatExtra("weight",0f)
        val hight = intent.getFloatExtra("hight",0f)
        val button = findViewById<Button>(R.id.btn_back)
        val bmi = weight / (hight * hight)

        textView.text="${name}您的身高:${hight*100}公分\n體重:${weight}公斤\nBMI為${bmi}"
        button.setOnClickListener {
            val intent = Intent().putExtra("BMI",bmi)
            intent.putExtra("name",name)
            intent.putExtra("weight",weight)
            intent.putExtra("hight",hight)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }
}