package com.example.androidfinal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidfinal.viewModel.UsersViewModel

class MainActivity : AppCompatActivity() {

    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        usersViewModel.registerUser("nadav".toString(), "nadavaza@gmail.com".toString(), password = "22222".toString())
        usersViewModel.currentUser.observe(this) { user ->
            if (user != null) {
                println("Logged in user: ${user.name}")
            } else {
                println("No user is logged in")
            }
        }
    }
}