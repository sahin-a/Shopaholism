package com.sar.shopaholism.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.presenter.WishSortPresenter

data class SelectionResult(
    val otherWish: Wish,
    var isPreferred: Boolean
)

class WishSortViewPagerAdapter(
    val presenter: WishSortPresenter,
    val mainWish: Wish,
    val otherWishes: List<Wish>
) : RecyclerView.Adapter<WishSortViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // main wish
        val mainCardView: MaterialCardView = itemView.findViewById(R.id.main_cardview)
        val mainWishImage: ImageView = itemView.findViewById(R.id.main_image)
        val mainWishTitle: TextView = itemView.findViewById(R.id.main_wish_title)

        // other wish
        val otherCardView: MaterialCardView = itemView.findViewById(R.id.other_cardview)
        val otherWishImage: ImageView = itemView.findViewById(R.id.other_wish_image)
        val otherWishTitle: TextView = itemView.findViewById(R.id.other_wish_title)

        val pageIndicator: TextView = itemView.findViewById(R.id.page_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wish_sort_item_viewpager, parent, false)

        return ViewHolder(view)
    }

    private fun addResult(result: SelectionResult, isPreferred: Boolean) {
        result.isPreferred = isPreferred
        presenter.addResult(result)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val otherWish: Wish = otherWishes[position]

        val result = SelectionResult(
            otherWish = otherWish,
            isPreferred = false
        )

        // isPreffered is false when the main wish has been selected
        val cardViewOnClick = { res: SelectionResult, isPreferred: Boolean ->
            addResult(res, isPreferred)
            presenter.showNextPage()
        }

        val checkCard = { checkMainCard: Boolean ->
            holder.mainCardView.isChecked = checkMainCard
            holder.otherCardView.isChecked = !checkMainCard
        }

        // restore selection if data available
        presenter.model.selectionResults.findLast { res -> res.otherWish.id == otherWish.id }?.let {
            checkCard(!it.isPreferred)
        }

        // main wish
        when (mainWish.imageUri.isNotBlank()) {
            true -> {
                holder.mainWishImage.setImageURI(Uri.parse(mainWish.imageUri))
                holder.mainWishImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            false -> {
                holder.mainWishImage.setImageResource(R.drawable.ic_no_image)
                holder.mainWishImage.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
        holder.mainWishTitle.text = mainWish.title
        holder.mainCardView.setOnClickListener {
            checkCard(true)
            cardViewOnClick(result, false)
        }

        // other wish
        holder.otherWishImage.setImageURI(Uri.parse(otherWish.imageUri))
        holder.otherWishTitle.text = otherWish.title
        holder.otherCardView.setOnClickListener {
            checkCard(false)
            cardViewOnClick(result, true)
        }

        val pageIndicatorText: String = holder.itemView.context.getString(R.string.page_indicator)
        holder.pageIndicator.text = String.format(pageIndicatorText, position + 1, itemCount)
    }

    override fun getItemCount(): Int {
        return otherWishes.size
    }

}