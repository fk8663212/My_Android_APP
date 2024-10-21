package com.example.librarysystem

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
        dbrw = MyDBHelp.MyDBHelper(this, "myDB", 1).writableDatabase
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        this.findViewById<ListView>(R.id.BookList).adapter = adapter
        setListener()
    }
    private fun setListener() {
        val ed_book = findViewById<EditText>(R.id.ET_bookname)
        val ed_price = findViewById<EditText>(R.id.ET_bookprice)

        findViewById<Button>(R.id.btn_newbook).setOnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1)
                showToast("書名價格欄位請勿留空")
            else
                try {
                    dbrw.execSQL(
                        "INSERT INTO myTable(book, price) VALUES(?,?)",
                        arrayOf(ed_book.text.toString(), ed_price.text.toString())
                    )
                    showToast("新增書名${ed_book.text}    價格${ed_price.text}")

                }catch (e: Exception){
                    showToast("新增失敗: ${e.message}")
                }
        }
        findViewById<Button>(R.id.btn_mod).setOnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1)
                showToast("書名價格欄位請勿留空")
            else
                try {
                    dbrw.execSQL("UPDATE myTable SET price = ${ed_price.text} WHERE book LIKE '${ed_book.text}'")
                    showToast("修改書名${ed_book.text}    價格${ed_price.text}")
                }catch (e: Exception){
                    showToast("修改失敗: ${e.message}")
                }

        }
        findViewById<Button>(R.id.btn_del).setOnClickListener {
            if (ed_book.length() < 1)
                showToast("書名欄位請勿留空")
            else{
                try {
                    dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '${ed_book.text}'")
                    showToast("刪除書名${ed_book.text}")
                }catch (e: Exception){
                    showToast("刪除失敗: ${e.message}")
                }

            }

        }
        findViewById<Button>(R.id.btn_find).setOnClickListener {
            if (ed_book.length() < 1)
                showToast("書名欄位請勿留空")
            else{
                try {
                    dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE '${ed_book.text}'", null).use {
                        cursor ->
                        items.clear()
                        showToast("查詢書名${ed_book.text},價格${ed_price.text}")
                        val c =dbrw.rawQuery("SELECT * FROM myTable", null)
                        for(i in 0 until cursor.count){
                            cursor.moveToNext()
                            items.add("書名:${cursor.getString(0)}				價格:${cursor.getInt(1)}")
                        }



                    }
                }catch (e: Exception){
                    showToast("查詢失敗: ${e.message}")
                }
            }
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