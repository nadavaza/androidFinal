package com.example.androidfinal.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.example.androidfinal.entities.Post
import com.example.androidfinal.entities.User
import com.example.androidfinal.utils.DateUtils
import com.squareup.picasso.Picasso

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
        private val imageViewPhoto: ImageView = view.findViewById(R.id.imageViewPhoto)
        private val buttonEditPost: ImageButton = view.findViewById(R.id.buttonEditPost)
        private val buttonDeletePost: ImageButton = view.findViewById(R.id.buttonDeletePost)

        fun bind(
            post: Post,
            onEditClick: (Post) -> Unit,
            onDeleteClick: (Post) -> Unit,
            user: User?,
            isProfileScreen: Boolean
        ) {
            // ✅ Display the correct username (fetch dynamically if needed)
            textViewUsername.text = user?.name ?: "Unknown User"

            textViewEpisodeTitle.text = if (post.title.length > 25) {
                "${post.title.substring(0, 25)}..."
            } else {
                post.title
            }

            textViewReview.text = post.review
            textViewRating.text = "⭐ ${post.rating}/10"
            textViewTimeStamp.text = DateUtils.formatTimestamp(post.timestamp)


            if (!post.photo.isNullOrEmpty()) {
                val imageUri = Uri.parse(post.photo)

                try {
                    // ✅ Try setting image directly (works for local images)
                    imageViewPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
                } catch (e: Exception) {
                    imageViewPhoto.setImageResource(R.drawable.noanime)
                }
            } else {
                imageViewPhoto.setImageResource(R.drawable.noanime)
            }

            // ✅ Show edit & delete buttons only if the post belongs to the current user
            if (post.userId == user?.id && isProfileScreen) {
                buttonEditPost.visibility = View.VISIBLE
                buttonDeletePost.visibility = View.VISIBLE
            } else {
                buttonEditPost.visibility = View.GONE
                buttonDeletePost.visibility = View.GONE
            }

            // ✅ Set listeners for edit & delete buttons
            buttonEditPost.setOnClickListener { onEditClick(post) }
            buttonDeletePost.setOnClickListener { onDeleteClick(post) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position], onEditClick, onDeleteClick, user, isProfileScreen)
    }

    override fun getItemCount() = posts.size
}
