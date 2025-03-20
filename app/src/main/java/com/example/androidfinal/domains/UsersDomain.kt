package com.example.androidfinal.domains

import com.example.androidfinal.entities.User
import com.example.androidfinal.repositories.User.FireBaseUserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UsersDomain(private val fireBaseUserRepository: FireBaseUserRepository) {

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
        callback: (User?) -> Unit
    ) {
        fireBaseUserRepository.registerUser(newUser, callback)
    }

    fun updateUser(userId: String, updatedUser: User, callback: (Boolean) -> Unit) {
        fireBaseUserRepository.updateUser(userId, updatedUser, callback)
    }

    fun logout() {
        fireBaseUserRepository.logout()
    }
}