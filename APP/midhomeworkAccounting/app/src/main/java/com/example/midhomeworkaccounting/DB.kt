package com.example.midhomeworkaccounting

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database").build()
                INSTANCE=instance
                instance
            }
        }
    }
}
