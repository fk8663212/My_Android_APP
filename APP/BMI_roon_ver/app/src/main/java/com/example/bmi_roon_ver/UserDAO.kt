package com.example.bmi_roon_ver

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET height = :height, weight = :weight, bmi = :bmi WHERE name = :name")
    suspend fun updateUser(name: String, height: Int, weight: Int, bmi: Double)

    @Query("DELETE FROM users WHERE name = :name")
    suspend fun deleteUser(name: String)

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

}