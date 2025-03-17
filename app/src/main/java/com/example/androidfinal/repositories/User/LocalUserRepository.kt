package com.example.androidfinal.repositories.User

import android.content.Context
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase

class LocalUserRepository(private val context: Context) {

    private val appLocalDB = AppDatabase.getDatabase(context)
    private val userDao = appLocalDB.userDao()

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User): Int {
        return userDao.updateUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }
}
