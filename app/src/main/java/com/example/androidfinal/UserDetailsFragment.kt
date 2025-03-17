package com.example.androidfinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class UserDetailsFragment : Fragment() {

    private lateinit var userProfileImage: ImageView
    private lateinit var editUserName: EditText
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewUserPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private var selectedImageUri: Uri? = null
    private val userPosts = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        userProfileImage = view.findViewById(R.id.userProfileImage)
        editUserName = view.findViewById(R.id.editUserName)
        buttonSave = view.findViewById(R.id.buttonSave)
        recyclerViewUserPosts = view.findViewById(R.id.recyclerViewUserPosts)

        // Load existing user data (mock data for now)
        editUserName.setText("John Doe")

        // Click listener to change profile picture
        userProfileImage.setOnClickListener {
            openGallery()
        }

        // Click listener to save changes
        buttonSave.setOnClickListener {
            saveUserDetails()
        }

        // Click listener to go back
        buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Initialize RecyclerView
        recyclerViewUserPosts.layoutManager = LinearLayoutManager(requireContext())

        // Load mock user posts
        loadUserPosts()
    }

    private fun loadUserPosts() {
        // Mock data: User's posts
        userPosts.addAll(
            listOf(
                Post("1", "John Doe", "Attack on Titan - Episode 12", 9, "Awesome episode!"),
                Post("2", "John Doe", "One Piece - Episode 1056", 8, "Great animation!"),
                Post("3", "John Doe", "Naruto Shippuden - Episode 500", 10, "Emotional ending.")
            )
        )

        // Set up adapter
        postAdapter = PostAdapter(userPosts)
        recyclerViewUserPosts.adapter = postAdapter
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveUserDetails() {
        val updatedName = editUserName.text.toString().trim()

        if (updatedName.isEmpty()) {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Mock saving process
        Toast.makeText(requireContext(), "User details updated!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                userProfileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
