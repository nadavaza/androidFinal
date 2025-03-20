package com.example.androidfinal.repositories.User

import com.example.androidfinal.entities.FireBaseModel
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.User.Companion.EMAIL_KEY
import com.example.androidfinal.entities.User.Companion.ID_KEY
import com.example.androidfinal.entities.User.Companion.LAST_UPDATED
import com.example.androidfinal.entities.User.Companion.NAME_KEY
import com.example.androidfinal.entities.User.Companion.PASSWORD_KEY
import com.example.androidfinal.entities.User.Companion.PHOTO_KEY
import com.example.androidfinal.entities.User.Companion.fromJSON
import com.google.firebase.auth.FirebaseUser

class FireBaseUserRepository {

    private val fireBase = FireBaseModel()

    fun getUser(callback: (User?) -> Unit) {
        val firebaseUser = fireBase.firebaseAuth.currentUser
        if (firebaseUser == null) {
            callback(null)
            return
        }

        fireBase.db.collection(FireBaseModel.USERS_COLLECTION_PATH)
            .document(firebaseUser.uid)
            .get()
            .addOnSuccessListener { document ->
                callback(document.data?.let { fromJSON(it) }) // Convert Firestore data to User
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        fireBase.db.collection(FireBaseModel.USERS_COLLECTION_PATH)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                callback(document.data?.let { fromJSON(it) }) // Convert Firestore data to User
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun login(
        email: String,
        password: String,
        callback: (FirebaseUser?) -> Unit
    ) {
        fireBase.firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result?.user)
                } else {
                    callback(null)
                }
            }
    }

    fun registerUser(
        newUser: User,
        callback: (User?) -> Unit
    ) {
        fireBase.firebaseAuth.createUserWithEmailAndPassword(newUser.email, newUser.password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener
                val userMap = mapOf(
                    ID_KEY to userId,
                    NAME_KEY to newUser.name,
                    EMAIL_KEY to newUser.email,
                    PASSWORD_KEY to newUser.password,
                    PHOTO_KEY to newUser.photo,
                    LAST_UPDATED to System.currentTimeMillis()
                )

                fireBase.db.collection(FireBaseModel.USERS_COLLECTION_PATH)
                    .document(userId)
                    .set(userMap)
                    .addOnSuccessListener { callback(fromJSON(userMap)) }
                    .addOnFailureListener { callback(null) }
            }
            .addOnFailureListener { callback(null) }
    }

    fun updateUser(userId: String, updatedUser: User, callback: (Boolean) -> Unit) {
        fireBase.db.collection(FireBaseModel.USERS_COLLECTION_PATH).document(userId)
            .update(updatedUser.json)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun logout() {
        fireBase.firebaseAuth.signOut()
    }
}