package com.example.moviezone.app.data.local.userdata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDetails(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val email: String,
)