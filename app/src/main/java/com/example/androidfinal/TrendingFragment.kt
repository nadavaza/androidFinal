package com.example.androidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrendingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var spinnerFilter: Spinner

    private val allPosts = listOf(
        Post("1", "AnimeFan123", "Attack on Titan - Episode 12", 9, "Amazing episode!"),
        Post("2", "OtakuKing", "One Piece - Episode 1056", 8, "Great animation!"),
        Post("3", "NarutoFan", "Naruto Shippuden - Episode 500", 10, "Best ending ever!"),
        Post("4", "WeebMaster", "Demon Slayer - Episode 19", 10, "Broke the internet!"),
        Post("5", "MangaReader", "Jujutsu Kaisen - Episode 24", 9, "Hyped battle!"),
        Post("6", "BakaSensei", "My Hero Academia - Episode 112", 7, "Good but slow."),
        Post("7", "OtakuLife", "Vinland Saga - Episode 20", 9, "Emotional!"),
    )

    private var filteredPosts = mutableListOf<Post>()

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
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeFrames)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = spinnerAdapter

        // Set default filter to "This Week"
        applyFilter("This Week")

        // Handle filter selection
        spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                applyFilter(timeFrames[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilter(filter: String) {
        // Simulate filtering logic (for now, we just sort by highest rating)
        filteredPosts = allPosts.sortedByDescending { it.rating }.toMutableList()

        // Refresh RecyclerView with the filtered list
        postAdapter = PostAdapter(filteredPosts)
        recyclerView.adapter = postAdapter
    }
}
