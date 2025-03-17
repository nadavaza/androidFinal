package com.example.androidfinal.repositories.Post

import android.content.Context
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase

class LocalPostRepository(context: Context) {
    private val postDao = AppDatabase.getDatabase(context).PostDao()

    suspend fun insertPost(post: Post) = postDao.insertPost(post)

    suspend fun getPostsByUser(userId: String): List<Post> = postDao.getPostsByUser(userId)

    suspend fun getAllPosts(): List<Post> = postDao.getAllPosts()

    suspend fun updatePost(post: Post): Int = postDao.updatePost(post)

    suspend fun deletePost(post: Post) = postDao.deletePost(post)
}
