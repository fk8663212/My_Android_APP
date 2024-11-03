package com.example.midhomeworkaccounting

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    suspend fun insert(record: Record)
    @Query("SELECT * FROM record")
    fun getAllRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM record")
    suspend fun getAll(): List<Record>
    @Query("SELECT * FROM record WHERE date = :date")
    suspend fun getByDate(date: String): List<Record>
    @Query("SELECT SUM(CASE WHEN isIncome = 1 THEN money ELSE -money END) FROM record WHERE date = :date")
    suspend fun getSumByDate(date: String): Int

}