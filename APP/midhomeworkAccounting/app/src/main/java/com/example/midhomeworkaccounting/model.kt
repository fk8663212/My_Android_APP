package com.example.midhomeworkaccounting

data class DateHeader(val date: String, val totalAmount: Int)
data class TransactionRecord(val description: String, val amount: Int, val isIncome: Boolean)
