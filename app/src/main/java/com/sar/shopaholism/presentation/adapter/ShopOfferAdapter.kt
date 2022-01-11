package com.sar.shopaholism.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.productlookup.Store

class ShopOfferAdapter : ListAdapter<Store, ShopOfferAdapter.ViewHolder>(StoreDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var openShop: Button = view.findViewById(R.id.shop_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.related_product_shop_offer_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store: Store = getItem(position)

        holder.openShop.apply {
            text = context.getString(R.string.shop_name_with_price, store.name, store.price)

            setOnClickListener {
                val openUrlAction = Intent(Intent.ACTION_VIEW)
                openUrlAction.data = store.url.toUri()

                holder.itemView.context.startActivity(openUrlAction)
            }
        }
    }

}

object StoreDiffCallback : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.url == newItem.url
    }
}