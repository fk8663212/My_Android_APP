package com.example.bmi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.editTextText_name)
        val  editText2 = findViewById<EditText>(R.id.editTextText_weight)
        val  editText3 = findViewById<EditText>(R.id.editTextText_hight)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val  textView = findViewById<TextView>(R.id.textView3)

         button.setOnClickListener {
             if(editText.length()<1) {
                 textView.text = "請輸入姓名"
                 return@setOnClickListener
             }
             val name = editText.text.toString()

             if(editText2.length()<1) {
                 textView.text = "請輸入體重"
                 return@setOnClickListener
             }

             if(editText3.length()<1) {
                 textView.text = "請輸入身高"
                 return@setOnClickListener
             }

             val weight = editText2.text.toString().toFloat()
             val hight = editText3.text.toString().toFloat()
             val bmi = weight / (hight * hight)

             if(radioGroup.checkedRadioButtonId == R.id.radioButton_boy){
                 if(bmi<=18.5) {
                     textView.text = "結果:\n${name}男士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
                 }
                 else if(bmi <= 24) {
                     textView.text = "結果:\n" +
                             "${name}男士您好\n" +
                             "您的身高:${weight}\n" +
                             "體重:${hight}\n" +
                             "BMI: %.2f\n正常範圍".format(bmi)
                 }
                 else if(bmi >= 27) {
                     textView.text = "結果:\n" +
                             "${name}男士您好\n" +
                             "您的身高:${weight}\n" +
                             "體重:${hight}\n" +
                             "BMI: %.2f\n體重過重".format(bmi)
                 }
             }
             else{
                 if(bmi<=18.5) {
                     textView.text = "結果:\n${name}女士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
                 }
                 else if(bmi <= 24) {
                     textView.text = "結果:\n" +
                             "${name}女士您好\n" +
                             "您的身高:${weight}\n" +
                             "體重:${hight}\n" +
                             "BMI: %.2f\n正常範圍".format(bmi)
                 }
                 else if(bmi >= 27) {
                     textView.text = "結果:\n" +
                             "${name}女士您好\n" +
                             "您的身高:${weight}\n" +
                             "體重:${hight}\n" +
                             "BMI: %.2f\n體重過重".format(bmi)
                 }



             }



         }


    }
}