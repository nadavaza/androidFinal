package com.example.androidfinal.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Foreign Key to User
    val title: String,
    val review: String,
    val rating: Int, // 1-10 rating
    val photo: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class TrendingPost(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "postCount") val postCount: Int,
    @ColumnInfo(name = "avgRating") val avgRating: Float,
    @ColumnInfo(name = "photo") val photo: String? // ✅ Allow null values
)
