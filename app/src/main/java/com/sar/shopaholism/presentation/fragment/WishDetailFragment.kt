package com.sar.shopaholism.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.OnWikiPageListener
import com.sar.shopaholism.presentation.adapter.WikiPagesAdapter
import com.sar.shopaholism.presentation.presenter.WishDetailPresenter
import com.sar.shopaholism.presentation.view.WishDetailView
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 * Use the [WishDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishDetailFragment : Fragment(), WishDetailView, OnWikiPageListener {
    private var wishId: Long? = null

    private val presenter: WishDetailPresenter by inject()

    private lateinit var wishImageView: ImageView
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var wikiPagesView: RecyclerView
    private lateinit var loadingIndicatorView: ProgressBar
    private lateinit var errorIndicatorView: View

    private var wikiPages: List<WikiPage> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            wishId = it.getLong(ARG_WISH_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishImageView = view.findViewById(R.id.wish_image)
        titleView = view.findViewById(R.id.wish_title)
        descriptionView = view.findViewById(R.id.wish_description)
        wikiPagesView = view.findViewById(R.id.related_products)
        loadingIndicatorView = view.findViewById(R.id.progress_bar)
        errorIndicatorView = view.findViewById(R.id.no_products_found_error)
        wikiPagesView.adapter = WikiPagesAdapter(onWikiPageListener = this)

        presenter.attachView(this)
    }

    companion object {
        const val ARG_WISH_ID = "wishId"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param wishId Parameter Wish Id
         * @return A new instance of fragment WishDetailFragment.
         */
        @JvmStatic
        fun newInstance(wishId: Long) =
            WishDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_WISH_ID, wishId)
                }
            }
    }

    override fun getWishId(): Long {
        return wishId ?: -1
    }

    override fun setWishData(wish: Wish) {
        titleView.text = wish.title

        if (wish.imageUri.isNotEmpty()) {
            wishImageView.setImageURI(Uri.parse(wish.imageUri))
        } else {
            wishImageView.visibility = View.GONE
        }

        if (wish.description.isNotEmpty()) {
            descriptionView.text = wish.description
        } else {
            descriptionView.visibility = View.GONE
        }
    }

    override fun setWikiPages(wikiPages: List<WikiPage>) {
        this.wikiPages = wikiPages
        (wikiPagesView.adapter as WikiPagesAdapter).submitList(wikiPages)
    }

    override fun toggleLoadingIndicator(show: Boolean) {
        loadingIndicatorView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showError() {
        wikiPagesView.visibility = View.GONE
        errorIndicatorView.visibility = View.VISIBLE
    }

    override fun onWikiPageClick(position: Int) {
        val wikiPage = wikiPages[position]
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(wikiPage.htmlUrl))

        startActivity(intent)
    }
}