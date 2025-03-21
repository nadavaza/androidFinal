package com.example.androidfinal

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.example.androidfinal.entities.User
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel
import com.squareup.picasso.Picasso
import java.io.IOException

class UserDetailsFragment : Fragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val postsViewModel: PostsViewModel by activityViewModels()

    private lateinit var userProfileImage: ImageView
    private lateinit var editUserName: EditText
    private lateinit var buttonSave: Button
    private lateinit var recyclerViewUserPosts: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var editEpisodeTitle: EditText
    private lateinit var editReview: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var postPhoto: ImageView


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

                if (!currentUser.photo.isNullOrEmpty()) {
                    Picasso.get()
                        .load(currentUser.photo)
                        .placeholder(R.drawable.noanime)
                        .error(R.drawable.noanime)
                        .into(userProfileImage)
                } else {
                    userProfileImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }

                postsViewModel.getPostsByUser(currentUser.id)
                postsViewModel.posts.observe(viewLifecycleOwner) { posts ->
                    postAdapter = PostAdapter(
                        posts,
                        usersViewModel,
                        { post -> openEditPostDialog(post) },
                        { post -> onDeletePost(post) },
                        usersViewModel.currentUser.value,
                        true,
                        viewLifecycleOwner
                    )
                    recyclerViewUserPosts.adapter = postAdapter
                }
            }
        }

        userProfileImage.setOnClickListener {
            openGallery(false)
        }

        buttonSave.setOnClickListener {
            saveUserDetails()
        }

        recyclerViewUserPosts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun openGallery(isUpdate: Boolean) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val requestCode = if (isUpdate) POST_UPDATE_IMG_REQUEST else PICK_IMAGE_REQUEST
        startActivityForResult(intent, requestCode)
    }

    private fun saveUserDetails() {
        val updatedName = editUserName.text.toString().trim()
        if (updatedName.isEmpty()) {
            Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = usersViewModel.currentUser.value
        if (currentUser != null) {
            val updatedUser = currentUser.copy(
                name = updatedName
            )
            var selectedBitmap: Bitmap? = null
            userProfileImage.isDrawingCacheEnabled = true
            userProfileImage.buildDrawingCache()
            selectedBitmap = (userProfileImage.drawable as? BitmapDrawable)?.bitmap
            usersViewModel.updateUser(updatedUser, selectedBitmap)
        }

        Toast.makeText(requireContext(), "User details updated!", Toast.LENGTH_SHORT).show()
    }

    private fun openEditPostDialog(post: Post) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_post, null)
        editEpisodeTitle = dialogView.findViewById(R.id.editEpisodeTitle)
        editReview = dialogView.findViewById(R.id.editReview)
        ratingBar = dialogView.findViewById(R.id.ratingBar)
        postPhoto = dialogView.findViewById(R.id.imagePreview)

        postPhoto.setOnClickListener {
            openGallery(true)
        }

        editEpisodeTitle.setText(post.title)
        editReview.setText(post.review)
        ratingBar.rating = post.rating.toFloat()
        if (!post.photo.isNullOrEmpty()) {
            Picasso.get()
                .load(post.photo)
                .placeholder(R.drawable.noanime)
                .error(R.drawable.noanime)
                .into(postPhoto)
        } else {
            postPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }


        AlertDialog.Builder(requireContext())
            .setTitle("Edit Post")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedPost = post.copy(
                    title = editEpisodeTitle.text.toString(),
                    review = editReview.text.toString(),
                    rating = ratingBar.rating.toInt()
                )
                var selectedBitmap: Bitmap? = null
                postPhoto.isDrawingCacheEnabled = true
                postPhoto.buildDrawingCache()
                selectedBitmap = (postPhoto.drawable as? BitmapDrawable)?.bitmap
                postsViewModel.updatePost(updatedPost, selectedBitmap)
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
            userProfileImage.setImageURI(data.data)
            userProfileImage.visibility = View.VISIBLE
        } else if (requestCode == POST_UPDATE_IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            postPhoto.setImageURI(data.data)
            postPhoto.visibility = View.VISIBLE
        }

    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val POST_UPDATE_IMG_REQUEST = 2
    }
}
