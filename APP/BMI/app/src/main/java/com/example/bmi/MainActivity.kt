package com.example.bmi

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //data base
        dbrw = MyDBHelp.MyDBHelper(this, "myDB", 1).writableDatabase
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)




        val button = findViewById<Button>(R.id.btn_add)
        val editText = findViewById<EditText>(R.id.editTextText_name)
        val  editText2 = findViewById<EditText>(R.id.editTextText_weight)
        val  editText3 = findViewById<EditText>(R.id.editTextText_hight)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val  textView = findViewById<TextView>(R.id.textView3)
        val intent = Intent(this,MainActivity2::class.java)
        val button2 = findViewById<Button>(R.id.btn_rm)
        val button3 = findViewById<Button>(R.id.btn_mod)
        val button4 = findViewById<Button>(R.id.btn_find)



        val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                val bmi = it.data?.getFloatExtra("BMI",0.0f)
                val name = it.data?.getStringExtra("name")
                val weight = it.data?.getFloatExtra("weight",0.0f)
                val height = it.data?.getFloatExtra("hight",0.0f)


                try {
                    dbrw.execSQL("INSERT INTO myTable(username, bmi) VALUES(?, ?)", arrayOf(name.toString(), bmi.toString()))
                    showToast("新增成功")
                }catch (e: Exception){
                    showToast("新增失敗$e")

                    textView.text = e.toString()
                    return@registerForActivityResult
                }

                //textView.text = "BMI為${bmi}"

//                if(radioGroup.checkedRadioButtonId == R.id.radioButton_boy){
//                    if (bmi != null) {
//                        if(bmi<=18.5) {
//                            textView.text = "結果:\n${name}男士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
//                        } else if(bmi <= 24) {
//                            textView.text = "結果:\n" +
//                                    "${name}男士您好\n" +
//                                    "您的身高:${weight}\n" +
//                                    "體重:${hight}\n" +
//                                    "BMI: %.2f\n正常範圍".format(bmi)
//                        } else if(bmi >= 27) {
//                            textView.text = "結果:\n" +
//                                    "${name}男士您好\n" +
//                                    "您的身高:${weight}\n" +
//                                    "體重:${hight}\n" +
//                                    "BMI: %.2f\n體重過重".format(bmi)
//                        }
//                    }
//                }
//                else{
//                    if (bmi != null) {
//                        if(bmi<=18.5) {
//                            textView.text = "結果:\n${name}女士您好\n您的身高:${weight}\n體重:${hight}\nBMI: %.2f\n建議:體重過輕".format(bmi)
//                        } else if(bmi <= 24) {
//                            textView.text = "結果:\n" +
//                                    "${name}女士您好\n" +
//                                    "您的身高:${weight}\n" +
//                                    "體重:${hight}\n" +
//                                    "BMI: %.2f\n正常範圍".format(bmi)
//                        } else if(bmi >= 27) {
//                            textView.text = "結果:\n" +
//                                    "${name}女士您好\n" +
//                                    "您的身高:${weight}\n" +
//                                    "體重:${hight}\n" +
//                                    "BMI: %.2f\n體重過重".format(bmi)
//                        }
//                    }
//
//
//
//                }
            }
        }
        button2.setOnClickListener{
            if (editText.length() < 1 )
                showToast("名字請勿留空")
            else
                try {
                    dbrw.execSQL("DELETE FROM myTable WHERE username = ?", arrayOf(editText.text.toString()))
                }catch (e: Exception){
                    showToast("修改失敗: ${e.message}")
                }
        }

        button4.setOnClickListener{
            val c = dbrw.rawQuery("SELECT * FROM myTable", null)
            c.moveToFirst()
        }



        button.setOnClickListener {
            if(editText.length()<1) {
                textView.text = "請輸入姓名"
                showToast("請輸入姓名")

                return@setOnClickListener
            }

            else if(editText2.length()<1) {
                textView.text = "請輸入體重"
                showToast("請輸入體重")
                return@setOnClickListener
            }

            else if(editText3.length()<1) {
                textView.text = "請輸入身高"
                showToast("請輸入身高")
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
    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()

    }
    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}