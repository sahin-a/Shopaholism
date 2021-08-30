package com.sar.shopaholism.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish

class WishSortViewPagerAdapter(var mainWish: Wish, val otherWishes: List<Wish>) : RecyclerView.Adapter<WishSortViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainWishImage: ImageView = itemView.findViewById(R.id.main_image)
        val otherWishImage: ImageView = itemView.findViewById(R.id.other_wish_image)
        val pageIndicator: TextView = itemView.findViewById(R.id.page_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wish_sort_item_viewpager, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val otherWish: Wish = otherWishes[position]

        holder.mainWishImage.setImageURI(Uri.parse(mainWish.imageUri))
        holder.otherWishImage.setImageURI(Uri.parse(otherWish.imageUri))

        val pageIndicatorText: String = holder.itemView.context.getString(R.string.page_indicator)

        holder.pageIndicator.text = String.format(pageIndicatorText, position, itemCount)
    }

    override fun getItemCount(): Int {
        return otherWishes.size
    }

}