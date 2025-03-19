package com.example.androidfinal

import android.annotation.SuppressLint
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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.adapters.PostAdapter
import com.example.androidfinal.entities.Post
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel
import java.io.IOException

class UserDetailsFragment : Fragment() {
    private lateinit var userProfileImage: ImageView
    private lateinit var editUserName: EditText
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewUserPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var selectedImageUri: Uri? = null
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val postsViewModel: PostsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userProfileImage = view.findViewById(R.id.userProfileImage)
        editUserName = view.findViewById(R.id.editUserName)
        buttonSave = view.findViewById(R.id.buttonSave)
        recyclerViewUserPosts = view.findViewById(R.id.recyclerViewUserPosts)

        usersViewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser != null) {
                editUserName.setText(currentUser.name)

                postsViewModel.getPostsByUser(usersViewModel.currentUser.value!!.id)
                postsViewModel.posts.observe(viewLifecycleOwner) { posts ->
                    postAdapter = PostAdapter(
                        posts, // ✅ No need to filter manually
                        { post -> openEditPostDialog(post) },
                        { post -> onDeletePost(post) },
                        usersViewModel.currentUser.value,
                        true
                    )
                    recyclerViewUserPosts.adapter = postAdapter
                }
            }
        }

        userProfileImage.setOnClickListener {
            openGallery()
        }

        buttonSave.setOnClickListener {
            saveUserDetails()
        }

        recyclerViewUserPosts.layoutManager = LinearLayoutManager(requireContext())
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
        var user = usersViewModel.currentUser.value
        if (user != null) {
            user.name = updatedName
            usersViewModel.updateUser(user)
        }
        Toast.makeText(requireContext(), "User details updated!", Toast.LENGTH_SHORT).show()
    }

    private fun openEditPostDialog(post: Post) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_post, null)
        val editEpisodeTitle = dialogView.findViewById<EditText>(R.id.editEpisodeTitle)
        val editReview = dialogView.findViewById<EditText>(R.id.editReview)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        // Set existing values
        editEpisodeTitle.setText(post.title)
        editReview.setText(post.review)
        ratingBar.rating = post.rating.toFloat()

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedPost = post.copy(
                    title = editEpisodeTitle.text.toString(),
                    review = editReview.text.toString(),
                    rating = ratingBar.rating.toInt()
                )
                postsViewModel.updatePost(updatedPost)
                Toast.makeText(requireContext(), "Post updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onDeletePost(post: Post) {
        postsViewModel.deletePost(post)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    selectedImageUri
                )
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
