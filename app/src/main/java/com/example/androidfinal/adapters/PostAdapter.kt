package com.example.androidfinal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.utils.DateUtils

class PostAdapter(
    private val posts: List<Post>,
    private val onEditClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit,
    private val user: User?,
    private val isProfileScreen: Boolean
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewUsername: TextView = view.findViewById(R.id.textViewUsername)
        private val textViewEpisodeTitle: TextView = view.findViewById(R.id.textViewEpisodeTitle)
        private val textViewReview: TextView = view.findViewById(R.id.textViewReview)
        private val textViewRating: TextView = view.findViewById(R.id.textViewRating)
        private val textViewTimeStamp: TextView = view.findViewById(R.id.textViewTimeStamp)
        private val buttonEditPost: ImageButton = view.findViewById(R.id.buttonEditPost)
        private val buttonDeletePost: ImageButton = view.findViewById(R.id.buttonDeletePost)

        fun bind(
            post: Post,
            onEditClick: (Post) -> Unit,
            onDeleteClick: (Post) -> Unit,
            user: User?,
            isProfileScreen: Boolean
        ) {
            textViewUsername.text = "TestUser"
            textViewEpisodeTitle.text = post.title
            textViewReview.text = post.review
            textViewRating.text = "⭐ ${post.rating}/10"
            textViewTimeStamp.text = DateUtils.formatTimestamp(post.timestamp)
            if (post.userId == user?.id && isProfileScreen) {
                buttonEditPost.visibility = View.VISIBLE
                buttonDeletePost.visibility = View.VISIBLE
            } else {
                buttonEditPost.visibility = View.GONE
                buttonDeletePost.visibility = View.GONE
            }

            buttonEditPost.setOnClickListener {
                onEditClick(post)
            }

            buttonDeletePost.setOnClickListener {
                onDeleteClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onEditClick, onDeleteClick, user, isProfileScreen)
    }

    override fun getItemCount() = posts.size
}
