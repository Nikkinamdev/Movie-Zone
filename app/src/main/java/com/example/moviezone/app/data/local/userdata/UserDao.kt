package com.example.moviezone.app.data.local.userdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao

interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun registerUser(user: UserDetails)

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun loginUser(email: String): UserDetails?

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun checkEmail(
        email: String): UserDetails?
    @Query("SELECT * FROM user_table LIMIT 1")
    suspend fun getUser(): UserDetails?

    @Query("UPDATE user_table SET name = :name WHERE email = :email")
    suspend fun updateName(name: String, email: String)

    @Query("UPDATE user_table SET email = :newEmail WHERE email = :oldEmail")
    suspend fun updateEmail(oldEmail: String, newEmail: String)
    @Query("UPDATE user_table SET profileImageUri = :uri")
    suspend fun updateProfileImage(uri: String)

    @Query("DELETE FROM user_table")
    suspend fun clearUser()
}