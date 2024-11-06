package com.example.midhomeworkaccounting.RecyclerView

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.midhomeworkaccounting.ListItem
import com.example.midhomeworkaccounting.MainActivity3
import com.example.midhomeworkaccounting.R

class TransactionAdapter(private val items: List<ListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_DATE_HEADER = 0
        const val TYPE_TRANSACTION_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DATE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_header, parent, false)
                DateHeaderViewHolder(view)
            }
            TYPE_TRANSACTION_ITEM -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
                TransactionItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is DateHeaderViewHolder -> {
                val dateItem = item as ListItem.DateHeader
                holder.bind(dateItem)
            }
            is TransactionItemViewHolder -> {
                val transactionItem = item as ListItem.TransactionItem
                holder.bind(transactionItem)

            }
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.DateHeader -> TYPE_DATE_HEADER
            is ListItem.TransactionItem -> TYPE_TRANSACTION_ITEM
        }
    }

    // ViewHolder for DateHeader
    class DateHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = view.findViewById(R.id.tv_date)
        private val totalAmountText: TextView = view.findViewById(R.id.tv_total_amount)

        fun bind(item: ListItem.DateHeader) {
            dateText.text = item.date
            totalAmountText.text = item.total
        }
    }

    // ViewHolder for TransactionItem
    class TransactionItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val descriptionText: TextView = view.findViewById(R.id.tv_description)
        private val amountText: TextView = view.findViewById(R.id.tv_amount)

        fun bind(item: ListItem.TransactionItem) {
            descriptionText.text = item.description
            amountText.text = item.amount
            if (item.isIncome){
                amountText.text = "+"+item.amount
                amountText.setTextColor(Color.GREEN)
            }
            else {
                amountText.text = "-" + item.amount
                amountText.setTextColor(Color.RED)
            }
            //點擊事件切換至另一個activity3
            //傳送資料位置，以便在activity3進行刪除修改
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MainActivity3::class.java)
                intent.putExtra("record_id", item.recordId) // 传递record ID
                intent.putExtra("year", item.year)
                intent.putExtra("month", item.month -1) // Calendar month is 0-based
                intent.putExtra("day", item.day)
                (itemView.context as? AppCompatActivity)?.startActivityForResult(intent, 2)
            }
        }
    }
}

