package com.example.androidfinal.entities

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidfinal.base.MyApplication
import com.google.firebase.firestore.FieldValue
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    var name: String,
    val email: String,
    val password: String,
    val photo: String,
    var lastUpdated: Long? = null
) {
    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val PHOTO_KEY = "photo"
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

        fun fromJSON(json: Map<String, Any>): User {
            val id = json[ID_KEY]?.toString() ?: ""
            val name = json[NAME_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val password = json[PASSWORD_KEY] as? String ?: ""
            val photo = json[PHOTO_KEY] as? String ?: ""
            val lastUpdated = (json[LAST_UPDATED] as? Map<*, *>)?.get("seconds") as? Long
                ?: System.currentTimeMillis()

            return User(
                id = id,
                name = name,
                email = email,
                password = password,
                photo = photo,
                lastUpdated = lastUpdated
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                NAME_KEY to name,
                EMAIL_KEY to email,
                PASSWORD_KEY to password,
                PHOTO_KEY to photo,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}
