package com.example.androidfinal.viewModel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.domains.UsersDomain
import com.example.androidfinal.entities.CloudinaryModel
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase
import com.example.androidfinal.repositories.Post.FireBasePostRepository
import com.example.androidfinal.repositories.User.FireBaseUserRepository
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val fireBaseUserRepository = FireBaseUserRepository()
    private val cloudinaryModel = CloudinaryModel()
    private val usersDomain = UsersDomain(fireBaseUserRepository, cloudinaryModel)

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    fun registerUser(newUser: User, photo: Bitmap?) {
        usersDomain.registerUser(newUser, photo) { user -> _currentUser.postValue(user) }
    }

    fun login(email: String, password: String) {
        usersDomain.login(email, password) { logged ->
            if (logged != null) {
                Post.lastUpdated = 0
                usersDomain.getUser { user -> _currentUser.postValue(user) }
            }
        }
    }

    fun logout() {
        usersDomain.logout()
        _currentUser.postValue(null)
    }

    fun getUser() {
        usersDomain.getUser { user -> _currentUser.postValue(user) }
    }

    fun getUserById(userId: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()

        usersDomain.getUserById(userId) { user ->
            userLiveData.postValue(user)
        }

        return userLiveData
    }

    fun updateUser(updatedUser: User) {
        val current = _currentUser.value
        if (current != null) {
            usersDomain.updateUser(current.id, updatedUser) { isUpdated ->
                if (isUpdated) {
                    _currentUser.postValue(updatedUser)
                }
            }
        }
    }

}