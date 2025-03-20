package com.example.androidfinal

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.androidfinal.viewModel.UsersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setupWithNavController(navController)

        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val buttonSignOut = findViewById<Button>(R.id.buttonSignOut)

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

        usersViewModel.currentUser.observe(this) { user ->
            if (user != null && !user.photo.isNullOrEmpty()) {
                Picasso.get()
                    .load(user.photo)
                    .placeholder(R.drawable.noanime)
                    .error(R.drawable.noanime)
                    .into(profileImage)
            } else {
                profileImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }


        buttonSignOut.setOnClickListener {
            usersViewModel.logout()

            profileImage.setImageResource(android.R.drawable.ic_menu_gallery)

            navController.navigate(R.id.welcomeFragment)
        }

        profileImage.setOnClickListener {
            navController.navigate(R.id.userDetailsFragment)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_feed -> navController.navigate(R.id.nav_feed)
                R.id.menu_add -> navController.navigate(R.id.nav_add)
                R.id.menu_trending -> navController.navigate(R.id.nav_trending)
            }
            true
        }
    }
}
