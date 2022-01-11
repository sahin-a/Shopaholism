package com.sar.shopaholism.presentation.fragment

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
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.presentation.adapter.RelatedProductsAdapter
import com.sar.shopaholism.presentation.presenter.WishDetailPresenter
import com.sar.shopaholism.presentation.view.WishDetailView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [WishDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishDetailFragment : Fragment(), WishDetailView {
    private var wishId: Long? = null

    private val presenter: WishDetailPresenter by inject()

    // Views
    private lateinit var productImageView: ImageView
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var relatedProductsView: RecyclerView
    private lateinit var loadingIndicatorView: ProgressBar
    private lateinit var errorIndicatorView: View

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

        presenter.attachView(this)

        productImageView = view.findViewById(R.id.wish_image)
        titleView = view.findViewById(R.id.wish_title)
        descriptionView = view.findViewById(R.id.wish_description)
        relatedProductsView = view.findViewById(R.id.related_products)
        loadingIndicatorView = view.findViewById(R.id.progress_bar)
        errorIndicatorView = view.findViewById(R.id.no_products_found_error)

        CoroutineScope(Dispatchers.Default).launch {
            presenter.loadData()
        }
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
        // TODO: Rename and change types and number of parameters
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

    override fun showData(wish: Wish, relatedProducts: List<Product>) {
        if (wish.imageUri.isEmpty()) {
            productImageView.setImageResource(R.drawable.no_image_placeholder)
        } else {
            productImageView.setImageURI(Uri.parse(wish.imageUri))
        }
        titleView.text = wish.title
        descriptionView.text = wish.description

        if (descriptionView.text.isEmpty()) {
            descriptionView.visibility = View.GONE
        }

        if (relatedProducts.isNotEmpty()) {
            val adapter = RelatedProductsAdapter()
            adapter.submitList(relatedProducts)

            relatedProductsView.adapter = adapter
        } else {
            showError()
        }
    }

    override fun toggleLoadingIndicator(show: Boolean) {
        loadingIndicatorView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showError() {
        relatedProductsView.visibility = View.GONE
        errorIndicatorView.visibility = View.VISIBLE
    }
}