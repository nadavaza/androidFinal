package com.example.androidfinal.repositories.Post

import com.example.androidfinal.entities.FireBaseModel
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.Post.Companion.LAST_UPDATED
import com.example.androidfinal.entities.Post.Companion.POST_ID
import com.example.androidfinal.entities.Post.Companion.RATING_KEY
import com.example.androidfinal.entities.Post.Companion.TIMESTAMP_KEY
import com.example.androidfinal.entities.Post.Companion.USER_ID_KEY
import com.example.androidfinal.entities.Post.Companion.fromJSON
import com.example.androidfinal.entities.TrendingPost
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query

class FireBasePostRepository {

    private val fireBase = FireBaseModel()

    fun getAllPosts(lastUpdated: Long, callback: (List<Post>) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(LAST_UPDATED, Timestamp(lastUpdated / 1000, 0))
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { doc ->
                    val data = doc.data
                    if (data != null) fromJSON(data) else null
                }
                callback(posts)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun getPostsByUser(userId: String, callback: (List<Post>) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .whereEqualTo(USER_ID_KEY, userId)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { doc ->
                    val data = doc.data
                    if (data != null) fromJSON(data) else null
                }
                callback(posts)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun getTrendingPosts(startTime: String, callback: (List<TrendingPost>) -> Unit) {
        val currentTime = System.currentTimeMillis()

        val startTimeLong = when (startTime) {
            "week" -> currentTime - (7L * 24 * 60 * 60 * 1000)
            "month" -> currentTime - (30L * 24 * 60 * 60 * 1000)
            "year" -> currentTime - (365L * 24 * 60 * 60 * 1000)
            else -> 0L
        }

        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { doc ->
                    val post = fromJSON(doc.data ?: emptyMap())
                    if (post.timestamp >= startTimeLong) post else null
                }

                // Group posts by title
                val trendingPosts = posts
                    .groupBy { it.title }
                    .map { (title, postList) ->
                        val postCount = postList.size
                        val avgRating = postList.map { it.rating }.average().toFloat()
                        val photo = postList.firstOrNull()?.photo ?: "" // Get first image

                        TrendingPost(
                            title = title,
                            postCount = postCount,
                            avgRating = avgRating,
                            photo = photo
                        )
                    }
                    .sortedByDescending { it.avgRating } // Sort by avg rating

                callback(trendingPosts)
            }
            .addOnFailureListener {
                callback(emptyList()) // Return empty list on failure
            }
    }

    fun insertPost(post: Post, callback: (Boolean) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .add(post.json)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePost(postId: String, updatedPost: Post, callback: (Boolean) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH).whereEqualTo(POST_ID, postId)
            .get()
            .addOnSuccessListener {
                fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
                    .document(it.documents[0].id)
                    .update(updatedPost.json)
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .whereEqualTo(POST_ID, post.id)
            .get()
            .addOnSuccessListener {
                fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
                    .document(it.documents[0].id)
                    .delete()
                    .addOnSuccessListener { callback(true) }
                    .addOnFailureListener { callback(false) }
            }
    }
}