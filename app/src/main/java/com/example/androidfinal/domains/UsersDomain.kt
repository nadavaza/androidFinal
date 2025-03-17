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
            if (loggedInUser != null && loggedInUser?.email == email) {
                callback(loggedInUser)
                return@launch
            }

            val user = localUserRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
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

    fun getLoggedInUser(callback: (User?) -> Unit) {
        coroutineScope.launch {
            if (loggedInUser != null) {
                callback(loggedInUser)
                return@launch
            }
        }
    }
}