package com.example.androidfinal

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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

        // Get profile image and sign out button
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val buttonSignOut = findViewById<Button>(R.id.buttonSignOut)

        // Hide Bottom Navigation, Profile Image, and Sign Out Button in Welcome, Sign In, and Sign Up screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment || destination.id == R.id.welcomeFragment) {
                bottomNavigationView.visibility = View.GONE
                profileImage.visibility = View.GONE
                buttonSignOut.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
                profileImage.visibility = View.VISIBLE
                buttonSignOut.visibility = View.VISIBLE
            }
        }

        // Sign Out Button Click (Navigates to Welcome Screen)
        buttonSignOut.setOnClickListener {
            // Simulate Logout
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show()

            // Navigate to Welcome Screen
            navController.navigate(R.id.welcomeFragment)
        }

        // Profile Picture Click (Navigates to User Details Screen)
        profileImage.setOnClickListener {
            navController.navigate(R.id.userDetailsFragment)
        }
    }
}
