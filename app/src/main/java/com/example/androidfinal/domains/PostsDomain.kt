package com.example.androidfinal.domains

import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.TrendingPost
import com.example.androidfinal.entities.User
import com.example.androidfinal.repositories.Post.LocalPostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostsDomain(private val localPostRepository: LocalPostRepository) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            localPostRepository.insertPost(post)
            callback(true)
        }
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        coroutineScope.launch {
            val posts = localPostRepository.getAllPosts()
            callback(posts)
        }
    }

    fun getPostsByUser(userId: String, callback: (List<Post>) -> Unit) {
        coroutineScope.launch {
            val posts = localPostRepository.getPostsByUser(userId)
            callback(posts)
        }
    }

    fun getTrendingPosts(timePeriod: String, callback: (List<TrendingPost>) -> Unit) {
        coroutineScope.launch {
            val trendingPosts = localPostRepository.getTrendingPosts(timePeriod)
            callback(trendingPosts)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            val updatedRows = localPostRepository.updatePost(post)
            callback(updatedRows > 0)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            localPostRepository.deletePost(post)
            callback(true)
        }
    }
}