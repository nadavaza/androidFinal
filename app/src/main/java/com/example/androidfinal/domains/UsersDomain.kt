package com.example.androidfinal.domains

import android.graphics.Bitmap
import com.example.androidfinal.entities.CloudinaryModel
import com.example.androidfinal.entities.User
import com.example.androidfinal.repositories.User.FireBaseUserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UsersDomain(
    private val fireBaseUserRepository: FireBaseUserRepository,
    private val cloudinaryModel: CloudinaryModel
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    fun getUser(callback: (User?) -> Unit) {
        fireBaseUserRepository.getUser(callback)
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        fireBaseUserRepository.getUserById(userId, callback)
    }

    fun login(email: String, password: String, callback: (FirebaseUser?) -> Unit) {
        fireBaseUserRepository.login(email, password, callback)
    }

    fun registerUser(
        newUser: User,
        photo: Bitmap?,
        callback: (User?) -> Unit
    ) {
        cloudinaryModel.uploadBitmap(photo, onSuccess = { imageUrl ->
            val updatedUser = newUser.copy(photo = imageUrl)
            fireBaseUserRepository.registerUser(updatedUser, callback)
        } , onError = {})

    }

    fun updateUser(userId: String, updatedUser: User, callback: (Boolean) -> Unit) {
        fireBaseUserRepository.updateUser(userId, updatedUser, callback)
    }

    fun logout() {
        fireBaseUserRepository.logout()
    }
}