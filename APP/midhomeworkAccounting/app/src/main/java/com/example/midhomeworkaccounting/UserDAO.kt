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
    //列出總共數量
    @Query("SELECT COUNT(*) FROM record")
    suspend fun getCount(): Int
    //列出所有資料
    @Query("SELECT * FROM record")
    suspend fun getAll(): List<Record>
    //刪除指定位置的資料
    @Query("DELETE FROM record WHERE id = :position")
    suspend fun delete(position: Int)
    //修改指定位置的資料
    @Query("UPDATE record SET name = :description, money = :amount,year = :year, month = :month, day = :day, isIncome = :isIncome WHERE id = :position ")
    suspend fun update(position: Int, description: String, amount: Int, year: Int, month: Int, day: Int, isIncome: Boolean)
    //查詢指定月份的資料
    @Query("SELECT * FROM record WHERE year = :year AND month = :month")
    suspend fun getByMonth(year: Int, month: Int): List<Record>

    @Query("SELECT * FROM record WHERE id = :recordId")
    suspend fun getRecordById(recordId: Int): Record














}