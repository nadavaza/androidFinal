package com.example.androidfinal

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.adapters.PostAdapter
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

        editUserName.setText("John Doe")

        // Click listener to change profile picture
        userProfileImage.setOnClickListener {
            openGallery()
        }

        // Click listener to save user details
        buttonSave.setOnClickListener {
            saveUserDetails()
        }

        // Click listener to go back
        buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Initialize RecyclerView
        recyclerViewUserPosts.layoutManager = LinearLayoutManager(requireContext())

        // Set adapter with edit callback
        postAdapter = PostAdapter(userPosts) { post -> openEditPostDialog(post) }
        recyclerViewUserPosts.adapter = postAdapter

        // Load user posts
        loadUserPosts()
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

        Toast.makeText(requireContext(), "User details updated!", Toast.LENGTH_SHORT).show()
    }

    private fun loadUserPosts() {
        userPosts.addAll(
            listOf(
                Post("1", "John Doe", "Attack on Titan - Episode 12", 9, "Awesome episode!"),
                Post("2", "John Doe", "One Piece - Episode 1056", 8, "Great animation!"),
                Post("3", "John Doe", "Naruto Shippuden - Episode 500", 10, "Emotional ending.")
            )
        )
        postAdapter.notifyDataSetChanged()
    }

    private fun openEditPostDialog(post: Post) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_post, null)
        val editEpisodeTitle = dialogView.findViewById<EditText>(R.id.editEpisodeTitle)
        val editReview = dialogView.findViewById<EditText>(R.id.editReview)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        // Set existing values
        editEpisodeTitle.setText(post.episodeTitle)
        editReview.setText(post.review)
        ratingBar.rating = post.rating.toFloat()

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Update the mutable properties of the post object
                post.episodeTitle = editEpisodeTitle.text.toString()
                post.review = editReview.text.toString()
                post.rating = ratingBar.rating.toInt()

                postAdapter.notifyDataSetChanged()  // Refresh the list
                Toast.makeText(requireContext(), "Post updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
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
