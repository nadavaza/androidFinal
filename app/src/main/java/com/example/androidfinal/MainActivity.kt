package com.example.androidfinal

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Get NavController from NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up Bottom Navigation with Navigation Component
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        // Get profile image
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        // Hide Bottom Navigation and Profile Image in Welcome, Sign In, and Sign Up screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment || destination.id == R.id.welcomeFragment) {
                bottomNavigationView.visibility = View.GONE
                profileImage.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
                profileImage.visibility = View.VISIBLE
            }
        }

        // Set up Profile Image Click Listener (Navigates to User Details Screen)
        profileImage.setOnClickListener {
            navController.navigate(R.id.userDetailsFragment)
        }
    }
}
