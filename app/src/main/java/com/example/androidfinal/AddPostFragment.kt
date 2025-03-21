package com.example.androidfinal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.androidfinal.entities.EpisodeInfo
import com.example.androidfinal.entities.Post
import com.example.androidfinal.viewModel.AnimeViewModel
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel

class AddPostFragment : Fragment() {

    private val postsViewModel: PostsViewModel by activityViewModels()
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val animeViewModel: AnimeViewModel by activityViewModels()

    private lateinit var spinnerEpisodeTitle: Spinner
    private lateinit var editTextReview: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var buttonSubmit: Button
    private lateinit var buttonChooseImage: Button
    private lateinit var imageViewPreview: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerEpisodeTitle = view.findViewById(R.id.spinnerEpisodeTitle)
        editTextReview = view.findViewById(R.id.editTextReview)
        ratingBar = view.findViewById(R.id.ratingBar)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        buttonChooseImage = view.findViewById(R.id.buttonChooseImage)
        imageViewPreview = view.findViewById(R.id.imageViewPreview)

        animeViewModel.episodes.observe(viewLifecycleOwner) { episodeInfos: List<EpisodeInfo> ->
            requireActivity().runOnUiThread {
                val episodes = episodeInfos.map { it.title }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    episodes
                )
                spinnerEpisodeTitle.adapter = adapter
            }
        }

        // Handle image selection
        buttonChooseImage.setOnClickListener {
            openGallery()
        }

        buttonSubmit.setOnClickListener {
            val episodeTitle = spinnerEpisodeTitle.selectedItem?.toString() ?: ""
            val review = editTextReview.text.toString()
            val rating = ratingBar.rating.toInt()
            var selectedBitmap: Bitmap? = null
            imageViewPreview.isDrawingCacheEnabled = true
            imageViewPreview.buildDrawingCache()
            selectedBitmap = (imageViewPreview.drawable as? BitmapDrawable)?.bitmap


            if (episodeTitle.isNotEmpty() && review.isNotEmpty()) {
                usersViewModel.currentUser.value?.let { currentUser ->
                    val newPost = Post(
                        userId = currentUser.id,
                        title = episodeTitle,
                        rating = rating,
                        review = review,
                        photo = ""
                    )

                    postsViewModel.addPost(newPost, selectedBitmap) {
                        requireActivity().runOnUiThread {
                            findNavController().navigate(R.id.nav_feed)
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageViewPreview.setImageURI(data.data)
            imageViewPreview.visibility = View.VISIBLE
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 100
    }
}
