package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.adapters.PostAdapter

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private val posts = mutableListOf(
        Post("1", "AnimeFan123", "Attack on Titan - Episode 12", 9, "Awesome episode!"),
        Post("2", "OtakuKing", "One Piece - Episode 1056", 8, "Great animation!"),
        Post("3", "NarutoFan", "Naruto Shippuden - Episode 500", 10, "Emotional ending."),
        Post("4", "WeebMaster", "Demon Slayer - Episode 19", 10, "The episode that broke the internet! 🔥")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        postAdapter = PostAdapter(posts) { post -> } // No edit functionality in HomeFragment
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter
    }

    // Function to add a new post dynamically
    fun addPost(newPost: Post) {
        posts.add(0, newPost) // Add new post at the top
        postAdapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0) // Scroll to the new post
    }
}
