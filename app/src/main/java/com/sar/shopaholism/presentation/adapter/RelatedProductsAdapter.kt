package com.sar.shopaholism.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.presentation.widgets.ImageCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class RelatedProductsAdapter :
    ListAdapter<Product, RelatedProductsAdapter.ViewHolder>(ProductDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageDownloadProgress: ProgressBar = view.findViewById(R.id.image_download_progress)
        var imageCard: ImageCardView = view.findViewById(R.id.image_view_card)
        var name: TextView = view.findViewById(R.id.product_name)
        var description: TextView = view.findViewById(R.id.product_description)
        var shopOffers: RecyclerView = view.findViewById(R.id.shop_offers)
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

        val adapter = ShopOfferAdapter()
        adapter.submitList(product.stores)
        viewHolder.shopOffers.adapter = adapter

        viewHolder.apply {
            name.text = product.title
            description.text = product.description
            product.images.firstOrNull()?.let { imageUrl ->
                Picasso.get()
                    .load(imageUrl)
                    .into(imageCard.imageView, object : Callback {
                        override fun onSuccess() {
                            imageDownloadProgress.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                        }
                    })
            }
        }

    }

}

object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.title == newItem.title
    }
}