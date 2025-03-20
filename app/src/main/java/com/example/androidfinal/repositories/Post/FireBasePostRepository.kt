package com.example.androidfinal.repositories.Post

import com.example.androidfinal.entities.FireBaseModel
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.Post.Companion.LAST_UPDATED
import com.example.androidfinal.entities.Post.Companion.POST_ID
import com.example.androidfinal.entities.Post.Companion.RATING_KEY
import com.example.androidfinal.entities.Post.Companion.TIMESTEMP_KEY
import com.example.androidfinal.entities.Post.Companion.USER_ID_KEY
import com.example.androidfinal.entities.Post.Companion.fromJSON
import com.example.androidfinal.entities.TrendingPost
import com.google.firebase.firestore.Query

class FireBasePostRepository {

    private val fireBase = FireBaseModel()

    fun getAllPosts(lastUpdated: Long, callback: (List<Post>) -> Unit) {
        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(LAST_UPDATED, lastUpdated)
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
        val startTimeLong = startTime.toLongOrNull() ?: 0L

        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(TIMESTEMP_KEY, startTimeLong)
            .orderBy(TIMESTEMP_KEY, Query.Direction.DESCENDING)
            .orderBy(RATING_KEY, Query.Direction.DESCENDING)
            .limit(50)
            .get()
            .addOnSuccessListener { result ->
                val posts = result.documents.mapNotNull { doc ->
                    fromJSON(doc.data ?: emptyMap())
                }

                val trendingPosts = posts
                    .groupBy { it.title }
                    .map { (title, postList) ->
                        val postCount = postList.size
                        val avgRating = postList.map { it.rating }.average().toFloat()
                        val photo = ""

                        TrendingPost(
                            title = title,
                            postCount = postCount,
                            avgRating = avgRating,
                            photo = photo
                        )
                    }
                    .sortedByDescending { it.avgRating }

                callback(trendingPosts)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(emptyList())
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