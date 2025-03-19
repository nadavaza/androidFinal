//package com.example.androidfinal.repositories.Post
//
//import com.example.androidfinal.entities.FireBaseModel
//import com.example.androidfinal.entities.Post
//import com.example.androidfinal.entities.Post.Companion.RATING_KEY
//import com.example.androidfinal.entities.Post.Companion.TIMESTEMP_KEY
//import com.example.androidfinal.entities.Post.Companion.USER_ID_KEY
//import com.example.androidfinal.entities.Post.Companion.fromJSON
//import com.example.androidfinal.entities.TrendingPost
//import com.google.firebase.firestore.Query
//
//class FireBasePostRepository {
//
//    private val fireBase = FireBaseModel()
//
//    fun getAllPosts(callback: (List<Post>) -> Unit) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
//            .get()
//            .addOnSuccessListener { result ->
//                val posts = result.documents.mapNotNull { doc ->
//                    val data = doc.data
//                    if (data != null) fromJSON(data) else null
//                }
//                callback(posts)
//            }
//            .addOnFailureListener {
//                callback(emptyList())
//            }
//    }
//
//    fun getPostsByUser(userId: Int, callback: (List<Post>) -> Unit) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
//            .whereEqualTo(USER_ID_KEY, userId)
//            .get()
//            .addOnSuccessListener { result ->
//                val posts = result.documents.mapNotNull { doc ->
//                    val data = doc.data
//                    if (data != null) fromJSON(data) else null
//                }
//                callback(posts)
//            }
//            .addOnFailureListener {
//                callback(emptyList())
//            }
//    }
//
//    fun getTrendingPosts(startTime: Long, callback: (List<Post>) -> Unit) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH)
//            .whereGreaterThanOrEqualTo(TIMESTEMP_KEY, startTime)
//            .orderBy(RATING_KEY, Query.Direction.DESCENDING)
//            .limit(50)
//            .get()
//            .addOnSuccessListener { result ->
//                val posts = result.documents.mapNotNull { doc ->
//                    val data = doc.data
//                    if (data != null) fromJSON(data) else null
//                }
//                callback(posts)
//            }
//            .addOnFailureListener {
//                callback(emptyList())
//            }
//    }
//
//    fun insertPost(post: Post) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH).add(post.json)
//    }
//
//    fun updatePost(post: Post, updatedPost: Post) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH).document(post.id.toString())
//            .update(updatedPost.json)
//    }
//
//    fun deletePost(post: Post) {
//        fireBase.db.collection(FireBaseModel.POSTS_COLLECTION_PATH).document(post.id.toString())
//            .delete()
//    }
//}