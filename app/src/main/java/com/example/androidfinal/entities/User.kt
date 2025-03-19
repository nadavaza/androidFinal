package com.example.androidfinal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    val email: String,
    val password: String,
    var profileImageUri: String? = null // New field for profile image URI
)
