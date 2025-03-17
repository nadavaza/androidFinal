package com.example.androidfinal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.domains.UsersDomain
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase
import com.example.androidfinal.repositories.User.LocalUserRepository
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application){

    private val localUserRepository = LocalUserRepository(application)
    private val usersDomain = UsersDomain(localUserRepository)

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> get() = _currentUser

    init {
        checkIfUserLoggedIn()
    }

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            val newUser = User(name = name, email = email, password = password)
            usersDomain.registerUser(newUser) { success ->
                _loginStatus.postValue(success)
                if (success) {
                    _currentUser.postValue(newUser)
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            usersDomain.login(email, password) { user ->
                if (user != null) {
                    _currentUser.postValue(user)
                    _loginStatus.postValue(true)
                } else {
                    _loginStatus.postValue(false)
                }
            }
        }
    }

    fun logout() {
        usersDomain.logout()
        _currentUser.postValue(null)
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            usersDomain.updateUser(user) { success ->
                if (success) {
                    _currentUser.postValue(user)
                }
            }
        }
    }

    private fun checkIfUserLoggedIn() {
        viewModelScope.launch {
            usersDomain.getLoggedInUser { user ->
                _currentUser.postValue(user)
            }
        }
    }
}