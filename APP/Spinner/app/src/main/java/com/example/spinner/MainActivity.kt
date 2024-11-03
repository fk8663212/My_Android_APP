package com.example.spinner

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        val fruits= listOf("Apple","Mango","Orange","Banana")

        val spinner = findViewById<Spinner>(R.id.spinner)
        val result = findViewById<TextView>(R.id.TV_result)

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,fruits)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter

        spinner.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                result.text = "Selected fruit: $selectedItem"
                // Do something with the selected item
            }

            fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "No fruit selected"
                // Handle the case where nothing is selected
            }
        }

    }
}