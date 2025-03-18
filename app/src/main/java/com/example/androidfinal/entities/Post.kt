package com.example.androidfinal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign Key to User
    val title: String,
    val review: String,
    val rating: Int, // 1-10 rating
    val timestamp: Long = System.currentTimeMillis()
)
