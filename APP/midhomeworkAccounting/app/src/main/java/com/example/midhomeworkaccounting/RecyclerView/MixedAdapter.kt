package com.example.midhomeworkaccounting.RecyclerView

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
        return if (items[position] is DateHeader) VIEW_TYPE_DATE_HEADER else VIEW_TYPE_RECORD
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
        if (holder is DateHeaderViewHolder) {
            val dateHeader = items[position] as DateHeader
            holder.tvDate.text = dateHeader.date
            holder.tvTotalAmount.text = "$${dateHeader.totalAmount}"
        } else if (holder is RecordViewHolder) {
            val record = items[position] as TransactionRecord
            holder.tvDescription.text = record.description
            holder.tvAmount.text = if (record.isIncome) "+$${record.amount}" else "-$${record.amount}"
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
