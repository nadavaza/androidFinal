package com.example.androidfinal.repositories.Post

import android.content.Context
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.TrendingPost
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalPostRepository(context: Context) {
    private val postDao = AppDatabase.getDatabase(context).PostDao()

    suspend fun getAllPosts(): List<Post> = postDao.getAllPosts()

    suspend fun getPostsByUser(userId: String): List<Post> = postDao.getPostsByUser(userId)

    suspend fun getTrendingPosts(timePeriod: String): List<TrendingPost> {
        val currentTime = System.currentTimeMillis()

        val startTime = when (timePeriod) {
            "week" -> currentTime - (7L * 24 * 60 * 60 * 1000)
            "month" -> currentTime - (30L * 24 * 60 * 60 * 1000)
            "year" -> currentTime - (365L * 24 * 60 * 60 * 1000)
            else -> 0L
        }

        return if (timePeriod == "all") {
            postDao.getAllTimeTrendingPosts()
        } else {
            postDao.getTrendingPosts(startTime)
        }
    }

    suspend fun insertPost(post: Post) = postDao.insertPost(post)

    suspend fun updatePost(post: Post): Int = postDao.updatePost(post)

    suspend fun deletePost(post: Post) = postDao.deletePost(post)

    fun clearAndInsert(firebasePosts: List<Post>) {
        CoroutineScope(Dispatchers.IO).launch {
            postDao.clearAllPosts()
            postDao.insertPosts(firebasePosts)
        }
    }
}
