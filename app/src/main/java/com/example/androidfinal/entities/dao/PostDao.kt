package com.example.androidfinal.entities.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.TrendingPost
import com.example.androidfinal.entities.User

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getPostsByUser(userId: String): List<Post>

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    suspend fun getAllPosts(): List<Post>

    @Query(
        """
    SELECT p.title, 
           COUNT(p.id) AS postCount,
           AVG(p.rating) AS avgRating
    FROM posts p
    WHERE p.timestamp >= :startTime
    GROUP BY p.title
    ORDER BY avgRating DESC
    LIMIT 50
"""
    )
    suspend fun getTrendingPosts(startTime: Long): List<TrendingPost>

    @Query(
        """
    SELECT p.title, 
           COUNT(p.id) AS postCount,
           AVG(p.rating) AS avgRating
    FROM posts p
    GROUP BY p.title
    ORDER BY avgRating DESC
    LIMIT 50
"""
    )
    suspend fun getAllTimeTrendingPosts(): List<TrendingPost>

    @Update
    suspend fun updatePost(post: Post): Int

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM posts")
    suspend fun clearAllPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)
}