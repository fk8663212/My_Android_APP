package com.example.bmi

<<<<<<< HEAD
import android.os.Build.VERSION_CODES.R
=======
import android.app.Activity
import android.content.Intent
>>>>>>> 94334609c3e24c591afeb3a7d4fa71764bc47ec4
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
        val intent = Intent(this,MainActivity2::class.java)


        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val bmi = it.data?.getFloatExtra("BMI",0.0f)
                val name = it.data?.getStringExtra("name")
                val weight = it.data?.getFloatExtra("weight",0.0f)
                val hight = it.data?.getFloatExtra("hight",0.0f)
                //textView.text = "BMI為${bmi}"

                if(radioGroup.checkedRadioButtonId == R.id.radioButton_boy){
                    if (bmi != null) {
                        if(bmi<=18.5) {
                            textView.text = "結果:\n${name}男士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
                        } else if(bmi <= 24) {
                            textView.text = "結果:\n" +
                                    "${name}男士您好\n" +
                                    "您的身高:${weight}\n" +
                                    "體重:${hight}\n" +
                                    "BMI: %.2f\n正常範圍".format(bmi)
                        } else if(bmi >= 27) {
                            textView.text = "結果:\n" +
                                    "${name}男士您好\n" +
                                    "您的身高:${weight}\n" +
                                    "體重:${hight}\n" +
                                    "BMI: %.2f\n體重過重".format(bmi)
                        }
                    }
                }
                else{
                    if (bmi != null) {
                        if(bmi<=18.5) {
                            textView.text = "結果:\n${name}女士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
                        } else if(bmi <= 24) {
                            textView.text = "結果:\n" +
                                    "${name}女士您好\n" +
                                    "您的身高:${weight}\n" +
                                    "體重:${hight}\n" +
                                    "BMI: %.2f\n正常範圍".format(bmi)
                        } else if(bmi >= 27) {
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



         button.setOnClickListener {
             if(editText.length()<1) {
                 textView.text = "請輸入姓名"
                 return@setOnClickListener
             }

             else if(editText2.length()<1) {
                 textView.text = "請輸入體重"
                 return@setOnClickListener
             }

             else if(editText3.length()<1) {
                 textView.text = "請輸入身高"
                 return@setOnClickListener
             }
             val name = editText.text.toString()
             val weight = editText2.text.toString().toFloat()
             val hight = editText3.text.toString().toFloat()/100
             intent.putExtra("name",name)
             intent.putExtra("weight",weight)
             intent.putExtra("hight",hight)

             getResult.launch(intent)





         }


    }
}