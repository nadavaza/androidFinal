package com.example.androidfinal

data class Post(
    val postId: String,
    val username: String,
    var episodeTitle: String,  // Changed to var to allow editing
    var rating: Int,           // Changed to var to allow editing
    var review: String         // Changed to var to allow editing
)
