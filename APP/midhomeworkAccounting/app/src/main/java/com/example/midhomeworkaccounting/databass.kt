package com.example.midhomeworkaccounting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "money") val money: Int=0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isIncome") val isIncome: Boolean

)