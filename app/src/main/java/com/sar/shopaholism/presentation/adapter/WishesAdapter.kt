package com.sar.shopaholism.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish

class WishesAdapter() :
    ListAdapter<Wish, WishesAdapter.ViewHolder>(WishesDiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wishTitleTextView: TextView = view.findViewById(R.id.wish_title)
        var wishDescriptionTextView: TextView = view.findViewById(R.id.wish_description)

        var wishEditButton: Button = view.findViewById(R.id.edit_button)
        var reprioritizeButton: Button = view.findViewById(R.id.reprioritize_button)

        var wishImageView: ImageView = view.findViewById(R.id.wish_image_view)

        init {
            // Define click listener for the ViewHolder's View.
            wishImageView.setImageResource(R.drawable.ic_launcher_foreground)

            wishEditButton.setOnClickListener {

            }

            reprioritizeButton.setOnClickListener {

            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.wish_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val wish: Wish = getItem(position)

        viewHolder.wishTitleTextView.text = wish.title
        viewHolder.wishDescriptionTextView.text = wish.description
    }
}


object WishesDiffCallback : DiffUtil.ItemCallback<Wish>() {
    override fun areItemsTheSame(oldItem: Wish, newItem: Wish): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Wish, newItem: Wish): Boolean {
        return oldItem.id == newItem.id
    }
}
