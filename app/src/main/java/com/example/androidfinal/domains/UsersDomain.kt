package com.example.androidfinal.domains

import com.example.androidfinal.entities.User
import com.example.androidfinal.repositories.User.LocalUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UsersDomain(private val localUserRepository: LocalUserRepository) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private var loggedInUser: User? = null


    fun registerUser(user: User, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            val existingUser = localUserRepository.getUserByEmail(user.email)
            if (existingUser == null) {
                localUserRepository.insertUser(user)
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun login(email: String, password: String, callback: (User?) -> Unit) {
        coroutineScope.launch {
            val user = localUserRepository.login(email, password)
            if (user != null) {
                loggedInUser = user
                callback(user)
            } else {
                callback(null)
            }
        }
    }

    fun logout() {
        loggedInUser = null
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        coroutineScope.launch {
            val existingUser = localUserRepository.getUserByEmail(user.email)
            if (existingUser != null) {
                localUserRepository.updateUser(user)
                loggedInUser = user
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        coroutineScope.launch {
            val loggedUser = localUserRepository.getUserByEmail(email)
            callback(loggedUser)
        }
    }
}