package com.example.midhomeworkaccounting

sealed class ListItem {
    data class DateHeader(val date: String, val total: String) : ListItem()
    data class TransactionItem(val recordId: Int, val description: String, val amount: String, val isIncome: Boolean, val year: Int, val month: Int, val day: Int) : ListItem()
}


