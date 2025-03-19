package com.example.androidfinal.domains

import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.TrendingPost
import com.example.androidfinal.repositories.Post.FireBasePostRepository
import com.example.androidfinal.repositories.Post.LocalPostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostsDomain(
    private val localPostRepository: LocalPostRepository,
    private val fireBasePostRepository: FireBasePostRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            localPostRepository.insertPost(post)
            fireBasePostRepository.insertPost(post) { success ->
                callback(success)
            }
        }
    }

    fun getAllPosts(lastUpdated: Long, callback: (List<Post>) -> Unit) {
        coroutineScope.launch {
            fireBasePostRepository.getAllPosts(lastUpdated) { firebasePosts ->
                if (firebasePosts.isNotEmpty()) {
                    localPostRepository.clearAndInsert(firebasePosts)
                    callback(firebasePosts)
                } else {
                    coroutineScope.launch {
                        val localPosts = localPostRepository.getAllPosts()
                        callback(localPosts)
                    }
                }
            }
        }
    }

    fun getPostsByUser(userId: String, callback: (List<Post>) -> Unit) {
        coroutineScope.launch {
            fireBasePostRepository.getPostsByUser(userId) { firebasePosts ->
                if (firebasePosts.isNotEmpty()) {
                    localPostRepository.clearAndInsert(firebasePosts)
                    callback(firebasePosts)
                } else {
                    coroutineScope.launch {
                        val localPosts = localPostRepository.getPostsByUser(userId)
                        callback(localPosts)
                    }
                }
            }
        }
    }

    fun getTrendingPosts(timePeriod: String, callback: (List<TrendingPost>) -> Unit) {
        coroutineScope.launch {
            val cachedTrendingPosts = localPostRepository.getTrendingPosts(timePeriod)
            callback(cachedTrendingPosts)

            fireBasePostRepository.getTrendingPosts(timePeriod) { firebaseTrendingPosts ->
                if (firebaseTrendingPosts.isNotEmpty()) {
                    callback(firebaseTrendingPosts)
                }
            }
        }
    }

    fun updatePost(postId: String, updatedPost: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            fireBasePostRepository.updatePost(postId, updatedPost) { success ->
                if (success) {
                    coroutineScope.launch {
                        val updatedRows = localPostRepository.updatePost(updatedPost)
                        callback(updatedRows > 0)
                    }
                }
                callback(success)
            }
        }


    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            fireBasePostRepository.deletePost(post) { success ->
                if (success) {
                    coroutineScope.launch {
                        localPostRepository.deletePost(post)
                        callback(true)
                    }
                }
                callback(success)
            }
        }
    }
}