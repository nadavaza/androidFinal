package com.example.androidfinal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.example.androidfinal.models.Post

class PostAdapter(
    private val posts: List<Post>,
    private val onEditClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewUsername: TextView = view.findViewById(R.id.textViewUsername)
        private val textViewEpisodeTitle: TextView = view.findViewById(R.id.textViewEpisodeTitle)
        private val textViewReview: TextView = view.findViewById(R.id.textViewReview)
        private val textViewRating: TextView = view.findViewById(R.id.textViewRating)
        private val buttonEditPost: Button = view.findViewById(R.id.buttonEditPost)

        fun bind(post: Post, onEditClick: (Post) -> Unit) {
            textViewUsername.text = post.username
            textViewEpisodeTitle.text = post.episodeTitle
            textViewReview.text = post.review
            textViewRating.text = "⭐ ${post.rating}/10"

            // Show edit button only for the logged-in user (e.g., "John Doe")
            buttonEditPost.visibility = if (post.username == "John Doe") View.VISIBLE else View.GONE

            buttonEditPost.setOnClickListener {
                onEditClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onEditClick)
    }

    override fun getItemCount() = posts.size
}
