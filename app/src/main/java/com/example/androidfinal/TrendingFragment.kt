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
import com.example.androidfinal.adapters.TrendingPostAdapter
import com.example.androidfinal.entities.Post
import com.example.androidfinal.viewModel.PostsViewModel
import com.example.androidfinal.viewModel.UsersViewModel

class TrendingFragment : Fragment() {

    private val postsViewModel: PostsViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var TrendingPostAdapter: TrendingPostAdapter
    private lateinit var spinnerFilter: Spinner


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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val timeFrames = arrayOf("This Week", "Month", "Year", "All Time")
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeFrames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = spinnerAdapter

        applyFilter("This Week")

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

        postsViewModel.trendingPosts.observe(viewLifecycleOwner) { posts ->
            TrendingPostAdapter = TrendingPostAdapter(posts)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = TrendingPostAdapter
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
