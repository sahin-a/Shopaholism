package com.sar.shopaholism.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.presentation.widgets.ImageCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class WikiPagesAdapter(
    private val onWikiPageListener: OnWikiPageListener
) :
    ListAdapter<WikiPage, WikiPagesAdapter.ViewHolder>(WikiPageDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageDownloadProgress: ProgressBar = view.findViewById(R.id.image_download_progress)
        var imageCard: ImageCardView = view.findViewById(R.id.image_view_card)
        var name: TextView = view.findViewById(R.id.wikipage_name)
        var description: TextView = view.findViewById(R.id.wikipage_description)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.wikipage_row, viewGroup, false)

        return ViewHolder(view)
    }

    private fun ViewHolder.showContent(show: Boolean) {
        name.visibility = if (show) View.VISIBLE else View.INVISIBLE
        description.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun ViewHolder.showLoading(show: Boolean) {
        imageDownloadProgress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun ImageView.loadImageFromUrl(
        url: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (url.isEmpty()) {
            onError()
            return
        }

        Picasso.get()
            .load(url)
            .into(this, object : Callback {
                override fun onSuccess() = onSuccess()

                override fun onError(e: Exception?) = onError()
            })
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val wikiPage = getItem(position)

        viewHolder.apply {
            name.text = wikiPage.title
            description.text =
                wikiPage.description.ifBlank { itemView.resources.getString(R.string.no_wikipage_description_available) }
            imageCard.imageView.setImageResource(R.drawable.no_image_placeholder)

            showContent(false)

            imageCard.imageView.loadImageFromUrl(
                wikiPage.thumbnailUrl,
                onSuccess = {
                    showLoading(false)
                    showContent(true)
                },
                onError = {
                    showLoading(false)
                    showContent(true)
                }
            )

            this.itemView.setOnClickListener {
                onWikiPageListener.onWikiPageClick(adapterPosition)
            }
        }

    }

}

interface OnWikiPageListener {
    fun onWikiPageClick(position: Int)
}

object WikiPageDiffCallback : DiffUtil.ItemCallback<WikiPage>() {
    override fun areItemsTheSame(oldItem: WikiPage, newItem: WikiPage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WikiPage, newItem: WikiPage): Boolean {
        return oldItem.title == newItem.title
    }
}