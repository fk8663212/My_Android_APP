package com.example.midhomeworkaccounting.RecyclerView

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.midhomeworkaccounting.DateHeader
import com.example.midhomeworkaccounting.R
import com.example.midhomeworkaccounting.TransactionRecord

class MixedAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_DATE_HEADER = 0
        const val VIEW_TYPE_RECORD = 1
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is DateHeader -> VIEW_TYPE_DATE_HEADER
            is TransactionRecord -> VIEW_TYPE_RECORD
            else -> throw IllegalArgumentException("不支援的項目類型")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_header, parent, false)
            DateHeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
            RecordViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is DateHeaderViewHolder && item is DateHeader) {
//            holder.tvDate.text = item.
//            holder.tvTotalAmount.text = "$${item.totalAmount}"
//            holder.tvTotalAmount.setTextColor(if (item.totalAmount >= 0) Color.BLUE else Color.RED)
        } else if (holder is RecordViewHolder && item is TransactionRecord) {
            holder.tvDescription.text = item.description
            holder.tvAmount.text = if (item.isIncome) "+$${item.amount}" else "-$${item.amount}"
            holder.tvAmount.setTextColor(if (item.isIncome) Color.BLUE else Color.RED)
        } else {
            // 調試代碼，顯示不支援的項目類型
            Log.e("MixedAdapter", "不支援的項目類型: ${item::class.java.simpleName} at position $position")
            throw IllegalArgumentException("在位置 $position 的項目類型不支援")
        }
    }


    override fun getItemCount(): Int = items.size

    // ViewHolder for Date Header
    inner class DateHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvTotalAmount: TextView = view.findViewById(R.id.tv_total_amount)
    }

    // ViewHolder for Record
    inner class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val tvAmount: TextView = view.findViewById(R.id.tv_amount)
    }
}

