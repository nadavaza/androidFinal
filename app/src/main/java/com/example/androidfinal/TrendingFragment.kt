package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.adapters.PostAdapter
import com.example.androidfinal.entities.Post
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel

class TrendingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var spinnerFilter: Spinner
    private var filteredPosts = mutableListOf<Post>()
    private val postsViewModel: PostsViewModel by activityViewModels()
    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTrending)
        spinnerFilter = view.findViewById(R.id.spinnerFilter)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up Spinner (Dropdown Menu) for Time Filter
        val timeFrames = arrayOf("This Week", "Month", "Year", "All Time")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeFrames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = spinnerAdapter

        applyFilter("This Week")

        // Handle filter selection
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                applyFilter(timeFrames[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        postsViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter = PostAdapter(posts, {}, {}, usersViewModel.currentUser.value, false)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = postAdapter
        }
    }

    private fun applyFilter(filter: String) {
        val timePeriod = when (filter) {
            "This Week" -> "week"
            "Month" -> "month"
            "Year" -> "year"
            else -> "all"
        }

        postsViewModel.getTrendingPosts(timePeriod)
    }
}
