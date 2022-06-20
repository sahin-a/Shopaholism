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

data class SelectionResult(
    val otherWish: Wish,
    var isPreferred: Boolean
)

class WishSortViewPagerAdapter(
    private val results: List<SelectionResult>,
    val mainWish: Wish,
    val otherWishes: List<Wish>,
    val onVoteSubmitted: (vote: SelectionResult) -> Unit
) : RecyclerView.Adapter<WishSortViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainCardView: MaterialCardView = itemView.findViewById(R.id.main_cardview)
        val mainWishImage: ImageView = itemView.findViewById(R.id.main_image)
        val mainWishTitle: TextView = itemView.findViewById(R.id.main_wish_title)

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

    private fun ViewHolder.checkCard(mainCardSelected: Boolean) {
        mainCardView.isChecked = mainCardSelected
        otherCardView.isChecked = !mainCardSelected
    }

    private fun ViewHolder.resetCardSelection() {
        mainCardView.isChecked = false
        otherCardView.isChecked = false
    }

    private fun ViewHolder.restoreSelectionIfAvailable(otherWish: Wish) {
        val selection =
            results.findLast { it.otherWish.id == otherWish.id }
        when (selection != null) {
            true -> checkCard(!selection.isPreferred)
            else -> resetCardSelection()
        }
    }

    private fun onCardViewClicked(viewHolder: ViewHolder, res: SelectionResult) {
        viewHolder.checkCard(!res.isPreferred)
        onVoteSubmitted(res)
    }

    private fun ImageView.setImageFromWish(wish: Wish) {
        scaleType = ImageView.ScaleType.CENTER_CROP
        when (wish.imageUri.isNotBlank()) {
            true -> {
                setImageURI(Uri.parse(wish.imageUri))
            }
            false -> {
                setImageResource(R.drawable.no_image_placeholder)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val otherWish: Wish = otherWishes[position]
        val result = SelectionResult(
            otherWish = otherWish,
            isPreferred = false
        )
        holder.restoreSelectionIfAvailable(otherWish)

        holder.mainWishImage.setImageFromWish(mainWish)
        holder.mainWishTitle.text = mainWish.title
        holder.mainCardView.setOnClickListener {
            onCardViewClicked(holder, result.apply { isPreferred = false })
        }

        holder.otherWishImage.setImageFromWish(otherWish)
        holder.otherWishTitle.text = otherWish.title
        holder.otherCardView.setOnClickListener {
            onCardViewClicked(holder, result.apply { isPreferred = true })
        }

        val pageIndicatorText: String = holder.itemView.context.getString(R.string.page_indicator)
        holder.pageIndicator.text = String.format(pageIndicatorText, position + 1, itemCount)
    }

    override fun getItemCount(): Int {
        return otherWishes.size
    }

}
