package com.example.androidfinal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.example.androidfinal.entities.TrendingPost
import com.squareup.picasso.Picasso

class TrendingPostAdapter(private val trendingPosts: List<TrendingPost>) :
    RecyclerView.Adapter<TrendingPostAdapter.TrendingPostViewHolder>() {

    class TrendingPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTrendingTitle)
        private val textViewAvgRating: TextView = view.findViewById(R.id.textViewTrendingRating)
        private val textViewPostCount: TextView = view.findViewById(R.id.textViewTrendingCount)
        private val imageViewThumbnail: ImageView =
            view.findViewById(R.id.imageViewTrendingThumbnail)

        fun bind(trendingPost: TrendingPost) {
            textViewTitle.text = trendingPost.title
            textViewAvgRating.text = "⭐ ${roundToHalf(trendingPost.avgRating.toDouble())}/10 Avg"
            textViewPostCount.text = "${trendingPost.postCount} Posts"

            if (!trendingPost.photo.isNullOrEmpty()) {
                Picasso.get()
                    .load(trendingPost.photo)
                    .placeholder(R.drawable.noanime)
                    .error(R.drawable.noanime)
                    .into(imageViewThumbnail)
            }
        }

        // Function to round rating to nearest 0.5 or whole number
        private fun roundToHalf(value: Double): String {
            val roundedValue = (Math.round(value * 2) / 2.0) // Ensure rounding to .0 or .5
            return if (roundedValue % 1.0 == 0.0) {
                String.format("%.0f", roundedValue) // Whole number (e.g., 4)
            } else {
                String.format("%.1f", roundedValue) // Half number (e.g., 4.5)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingPostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trending_post, parent, false)
        return TrendingPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrendingPostViewHolder, position: Int) {
        holder.bind(trendingPosts[position])
    }

    override fun getItemCount() = trendingPosts.size
}
