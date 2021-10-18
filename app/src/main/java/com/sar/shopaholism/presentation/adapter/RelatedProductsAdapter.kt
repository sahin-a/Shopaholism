package com.sar.shopaholism.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.entity.productlookup.Store

class RelatedProductsAdapter :
    ListAdapter<Product, RelatedProductsAdapter.ViewHolder>(ProductDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var shopNameTextView: TextView = view.findViewById(R.id.shop_name)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.related_product_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val product = getItem(position)

        viewHolder.apply {
            shopNameTextView.text = product.title
        }

    }

}

object ProductDiffCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.url == newItem.url
    }
}