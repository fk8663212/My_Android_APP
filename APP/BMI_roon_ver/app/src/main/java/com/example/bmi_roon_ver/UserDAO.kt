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

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

}