package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AddPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextUsername = view.findViewById<EditText>(R.id.editTextUsername)
        val editTextEpisodeTitle = view.findViewById<EditText>(R.id.editTextEpisodeTitle)
        val editTextReview = view.findViewById<EditText>(R.id.editTextReview)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val username = editTextUsername.text.toString()
            val episodeTitle = editTextEpisodeTitle.text.toString()
            val review = editTextReview.text.toString()
            val rating = ratingBar.rating.toInt()

            if (username.isNotEmpty() && episodeTitle.isNotEmpty() && review.isNotEmpty()) {
                val newPost = Post(
                    postId = System.currentTimeMillis().toString(),
                    username = username,
                    episodeTitle = episodeTitle,
                    rating = rating,
                    review = review
                )

                Toast.makeText(requireContext(), "Post added!", Toast.LENGTH_SHORT).show()

                // Navigate back to Home (Feed)
                findNavController().navigate(R.id.nav_feed)
            } else {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
