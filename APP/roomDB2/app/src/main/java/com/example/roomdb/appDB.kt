package com.example.roomdb

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Insert
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE:AppDatabase?=null;

        fun getDB(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
