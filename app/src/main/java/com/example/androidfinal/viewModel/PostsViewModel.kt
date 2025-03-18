package com.example.androidfinal.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidfinal.domains.PostsDomain
import com.example.androidfinal.entities.Post
import com.example.androidfinal.repositories.Post.LocalPostRepository
//import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val localPostRepository = LocalPostRepository(application)
    private val postDomain = PostsDomain(localPostRepository)

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    init {
        getAllPosts()
    }

    fun getAllPosts() {
        viewModelScope.launch {
            postDomain.getAllPosts { postList ->
                _posts.postValue(postList)
            }
        }
    }

    fun getPostsByUser(userId: Int) {
        viewModelScope.launch {
            postDomain.getPostsByUser(userId) { posts -> _posts.postValue(posts) }
        }
    }

    fun getTrendingPosts(timePeriod: String) {
        viewModelScope.launch {
            postDomain.getTrendingPosts(timePeriod) { posts ->
                _posts.postValue(posts)
            }
        }
    }

    fun addPost(post: Post, onComplete: () -> Unit) {
        viewModelScope.launch {
            postDomain.addPost(post) { success ->
                if (success) {
                    getAllPosts()
                    onComplete()
                }

            }
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            postDomain.updatePost(post) { success ->
                if (success) getPostsByUser(post.userId)
            }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postDomain.deletePost(post) { success ->
                if (success) getPostsByUser(post.userId)
            }
        }
    }
}