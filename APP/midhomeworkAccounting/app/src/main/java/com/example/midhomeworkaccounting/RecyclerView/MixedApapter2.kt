package com.example.midhomeworkaccounting.RecyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.midhomeworkaccounting.AppDatabase
import com.example.midhomeworkaccounting.RecordDao

class MixedApapter2(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_DATE_HEADER = 0
        const val VIEW_TYPE_RECORD = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}