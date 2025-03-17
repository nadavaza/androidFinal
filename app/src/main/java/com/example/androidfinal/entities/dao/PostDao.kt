package com.example.androidfinal.entities.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getPostsByUser(userId: String): List<Post>

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<Post>

    @Update
    suspend fun updatePost(post: Post): Int

    @Delete
    suspend fun deletePost(post: Post)
}