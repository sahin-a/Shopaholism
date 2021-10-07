package com.sar.shopaholism.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.fragment.DeleteWishFragmentDialog
import com.sar.shopaholism.presentation.fragment.WishesOverviewFragmentDirections

class WishesAdapter(
    private val context: Fragment
) :
    ListAdapter<Wish, WishesAdapter.ViewHolder>(WishesDiffCallback) {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wishTitleTextView: TextView = view.findViewById(R.id.wish_title)
        var wishDescriptionTextView: TextView = view.findViewById(R.id.wish_description)
        var wishPriorityTextView: TextView = view.findViewById(R.id.wish_priority)
        var wishPriceTextView: TextView = view.findViewById(R.id.wish_price)

        var deleteWishButton: FloatingActionButton = view.findViewById(R.id.delete_button)

        var wishEditButton: Button = view.findViewById(R.id.edit_button)
        var sortButton: Button = view.findViewById(R.id.reprioritize_button)

        var wishImageView: ImageView = view.findViewById(R.id.wish_image)
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

        when (wish.imageUri.isNotBlank()) {
            true -> {
                viewHolder.wishImageView.setImageURI(Uri.parse(wish.imageUri))
                viewHolder.wishImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            false -> {
                viewHolder.wishImageView.setImageResource(R.drawable.no_image_placeholder)
                viewHolder.wishImageView.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        viewHolder.wishTitleTextView.text = wish.title

        when (wish.description.isNotBlank()) {
            true -> {
                viewHolder.wishDescriptionTextView.text = wish.description
                viewHolder.wishDescriptionTextView.visibility = View.VISIBLE
            }
            false -> viewHolder.wishDescriptionTextView.visibility = View.GONE
        }

        viewHolder.wishPriorityTextView.text = wish.priority.toString()

        viewHolder.wishPriceTextView.text = wish.price.toString()
        viewHolder.wishPriceTextView.visibility = if (wish.price <= 0.0)
            View.GONE
        else
            View.VISIBLE

        viewHolder.wishEditButton.setOnClickListener {
            val action =
                WishesOverviewFragmentDirections.actionWishesOverviewFragmentToEditWishFragment(wish.id)

            viewHolder.itemView.findNavController()
                .navigate(action)
        }

        viewHolder.sortButton.setOnClickListener {
            val action =
                WishesOverviewFragmentDirections.actionWishesOverviewFragmentToSortWishFragment(wish.id)

            viewHolder.itemView.findNavController()
                .navigate(action)
        }

        viewHolder.sortButton.isEnabled = itemCount > 1

        viewHolder.deleteWishButton.setOnClickListener {
            val deleteDialog = DeleteWishFragmentDialog.newInstance(wish.id)
            deleteDialog.show(context.childFragmentManager, "delete_wish")
        }
    }
}


object WishesDiffCallback : DiffUtil.ItemCallback<Wish>() {
    override fun areItemsTheSame(oldItem: Wish, newItem: Wish): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Wish, newItem: Wish): Boolean {
        return oldItem.title == newItem.title
    }
}
