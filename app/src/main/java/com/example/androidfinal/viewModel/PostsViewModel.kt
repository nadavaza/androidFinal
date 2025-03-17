package com.example.androidfinal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.domains.PostsDomain
import com.example.androidfinal.domains.UsersDomain
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.entities.dao.AppDatabase
import com.example.androidfinal.repositories.Post.LocalPostRepository
import com.example.androidfinal.repositories.User.LocalUserRepository
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val localPostRepository = LocalPostRepository(application)
    private val postDomain = PostsDomain(localPostRepository)

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun loadAllPosts() {
        viewModelScope.launch {
            postDomain.getAllPosts { postList ->
                _posts.postValue(postList)
            }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            postDomain.addPost(post) { success ->
                if (success) loadAllPosts()
            }
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDomain.updatePost(post) { success ->
                if (success) loadAllPosts()
            }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDomain.deletePost(post) { success ->
                if (success) loadAllPosts()
            }
        }
    }
}