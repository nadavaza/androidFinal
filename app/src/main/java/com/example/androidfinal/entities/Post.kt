package com.example.androidfinal.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidfinal.base.MyApplication
import com.example.androidfinal.entities.User.Companion.ID_KEY
import com.google.firebase.firestore.FieldValue
import java.util.UUID

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: String,
    val userId: String, // Foreign Key to User
    val title: String,
    val review: String,
    val rating: Int, // 1-10 rating
    val photo: String,
    val timestamp: Long = System.currentTimeMillis(),
    var lastUpdated: Long? = null
) {
    companion object {
        const val POST_ID = "id"
        const val USER_ID_KEY = "userId"
        const val TITLE_KEY = "title"
        const val REVIEW_KEY = "review"
        const val RATING_KEY = "rating"
        const val PHOTO_KEY = "photo"
        const val TIMESTEMP_KEY = "timestamp"
        const val LAST_UPDATED = "lastUpdated"
        private const val GET_LAST_UPDATED = "get_last_updated"

        var lastUpdated: Long
            get() {
                return MyApplication.Globals
                    .context?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                MyApplication.Globals
                    ?.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                    ?.putLong(GET_LAST_UPDATED, value)?.apply()
            }

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY]?.toString() ?: ""
            val userId = json[USER_ID_KEY] as? String ?: ""
            val title = json[TITLE_KEY] as? String ?: ""
            val review = json[REVIEW_KEY] as? String ?: ""
            val rating = (json[RATING_KEY] as? Number)?.toInt() ?: 0
            val photo = json[PHOTO_KEY] as? String ?: ""
            val timestamp = when (val ts = json[TIMESTEMP_KEY]) {
                is Map<*, *> -> (ts["seconds"] as? Long)?.times(1000) ?: System.currentTimeMillis()
                is Long -> ts
                is Double -> ts.toLong()
                else -> System.currentTimeMillis()
            }
            val lastUpdated = (json[LAST_UPDATED] as? Map<*, *>)?.get("seconds") as? Long ?: 0

            return Post(
                id = id,
                userId = userId,
                title = title,
                review = review,
                rating = rating,
                photo = photo,
                timestamp = timestamp,
                lastUpdated = lastUpdated
            )
        }

    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                POST_ID to id,
                USER_ID_KEY to userId,
                TITLE_KEY to title,
                REVIEW_KEY to review,
                RATING_KEY to rating,
                PHOTO_KEY to PHOTO_KEY,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}

data class TrendingPost(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "postCount") val postCount: Int,
    @ColumnInfo(name = "avgRating") val avgRating: Float,
    @ColumnInfo(name = "photo") val photo: String?
)
