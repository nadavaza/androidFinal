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
        private val textViewEpisodeTitle: TextView = view.findViewById(R.id.textViewEpisodeTitle)
        private val textViewRating: TextView = view.findViewById(R.id.textViewRating)
        private val textViewPostCount: TextView = view.findViewById(R.id.textViewPostCount)
        private val imageViewPhoto: ImageView = view.findViewById(R.id.imageViewPhoto)

        fun bind(trendingPost: TrendingPost) {
            textViewEpisodeTitle.text = trendingPost.title
            textViewRating.text = "⭐ ${roundToHalf(trendingPost.avgRating.toDouble())}/10"
            textViewPostCount.text = "${trendingPost.postCount} reviews"

            if (!trendingPost.photo.isNullOrEmpty()) {
                Picasso.get()
                    .load(trendingPost.photo)
                    .placeholder(R.drawable.noanime)
                    .error(R.drawable.noanime)
                    .into(imageViewPhoto)
            } else {
                imageViewPhoto.setImageResource(R.drawable.noanime)
            }
        }

        private fun roundToHalf(value: Double): String {
            val roundedValue = (Math.round(value * 2) / 2.0)
            return if (roundedValue % 1.0 == 0.0) {
                String.format("%.0f", roundedValue)
            } else {
                String.format("%.1f", roundedValue)
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
