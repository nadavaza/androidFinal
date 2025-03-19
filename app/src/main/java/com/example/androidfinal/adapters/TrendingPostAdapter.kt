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
            textViewAvgRating.text = "⭐ ${trendingPost.avgRating}/10 Avg"
            textViewPostCount.text = "${trendingPost.postCount} Posts"

            if (trendingPost.photo?.isNotEmpty() == true)
                Picasso.get()
                    .load(trendingPost.photo)
                    .placeholder(R.drawable.noanime)
                    .error(R.drawable.noanime)
                    .into(imageViewThumbnail)
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
