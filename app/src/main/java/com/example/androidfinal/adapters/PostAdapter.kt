package com.example.androidfinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewUsername: TextView = view.findViewById(R.id.textViewUsername)
        private val textViewEpisodeTitle: TextView = view.findViewById(R.id.textViewEpisodeTitle)
        private val textViewReview: TextView = view.findViewById(R.id.textViewReview)
        private val textViewRating: TextView = view.findViewById(R.id.textViewRating)

        fun bind(post: Post) {
            textViewUsername.text = post.username
            textViewEpisodeTitle.text = post.episodeTitle
            textViewReview.text = post.review
            textViewRating.text = "⭐ ${post.rating}/10"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size
}
