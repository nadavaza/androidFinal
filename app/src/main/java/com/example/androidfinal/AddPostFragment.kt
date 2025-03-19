package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.room.PrimaryKey
import com.example.androidfinal.entities.EpisodeInfo
import com.example.androidfinal.entities.Post
import com.example.androidfinal.repositories.Anime.AnimeRepository
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel

class AddPostFragment : Fragment() {

    private val postsViewModel: PostsViewModel by activityViewModels()
    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerEpisodeTitle = view.findViewById<Spinner>(R.id.spinnerEpisodeTitle)
        val editTextReview = view.findViewById<EditText>(R.id.editTextReview)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)

        AnimeRepository().fetchAnimeList { episodeInfos: List<EpisodeInfo> ->
            requireActivity().runOnUiThread {
                val episodes = episodeInfos.map { "${it.title} - Episode ${it.epNum}" }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    episodes
                )
                spinnerEpisodeTitle.adapter = adapter
            }
        }

        buttonSubmit.setOnClickListener {
            val episodeTitle = spinnerEpisodeTitle.selectedItem?.toString() ?: ""
            val review = editTextReview.text.toString()
            val rating = ratingBar.rating.toInt()

            if (episodeTitle.isNotEmpty() && review.isNotEmpty()) {
                usersViewModel.currentUser.value?.let { currentUser ->
                    val newPost = Post(
                        userId = currentUser.id,
                        title = episodeTitle,
                        rating = rating,
                        review = review,
                        photo = ""
                    )

                    postsViewModel.addPost(newPost) {
                        requireActivity().runOnUiThread {
                            findNavController().navigate(R.id.nav_feed)
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
