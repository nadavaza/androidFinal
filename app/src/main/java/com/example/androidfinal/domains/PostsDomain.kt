package com.example.androidfinal.domains

import android.graphics.Bitmap
import com.example.androidfinal.entities.CloudinaryModel
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
    private val fireBasePostRepository: FireBasePostRepository,
    private val cloudinaryModel: CloudinaryModel
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
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
            fireBasePostRepository.getTrendingPosts(timePeriod) { firebaseTrendingPosts ->
                if (firebaseTrendingPosts.isNotEmpty()) {
                    callback(firebaseTrendingPosts)
                } else {
                    coroutineScope.launch {
                        val cachedTrendingPosts = localPostRepository.getTrendingPosts(timePeriod)
                        callback(cachedTrendingPosts)
                    }
                }
            }
        }
    }

    fun addPost(post: Post, photo: Bitmap?, callback: (Boolean) -> Unit) {
        cloudinaryModel.uploadBitmap(photo, onSuccess = { imageUrl ->
            val updatedPost = post.copy(photo = imageUrl)
            coroutineScope.launch {
                fireBasePostRepository.insertPost(updatedPost) { success ->
                    if (success) {
                        coroutineScope.launch {
                            localPostRepository.insertPost(updatedPost)
                        }
                    }
                    callback(success)
                }
            }
        }, onError = {
            callback(false)
        })
    }

    fun updatePost(postId: String, updatedPost: Post, photo: Bitmap?, callback: (Boolean) -> Unit) {
        cloudinaryModel.uploadBitmap(photo, onSuccess = { imageUrl ->
            val updatedPostWithPhoto = updatedPost.copy(photo = imageUrl)
            coroutineScope.launch {
                fireBasePostRepository.updatePost(postId, updatedPostWithPhoto) { success ->
                    if (success) {
                        coroutineScope.launch {
                            localPostRepository.updatePost(updatedPostWithPhoto)
                        }
                    }
                    callback(success)
                }
            }
        }, onError = {
            callback(false)
        })
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