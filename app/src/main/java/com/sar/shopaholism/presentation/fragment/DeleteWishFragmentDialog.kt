package com.sar.shopaholism.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.presenter.WishDeletionPresenter
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DeleteWishFragmentDialog : DialogFragment(), WishDeletionView {
    private val presenter: WishDeletionPresenter by inject()

    private lateinit var deleteButton: Button
    private lateinit var imageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var priceText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_delete_wish, container, false)

        imageView = view.findViewById(R.id.wish_image)
        titleText = view.findViewById(R.id.wish_title)
        descriptionText = view.findViewById(R.id.wish_description)
        priceText = view.findViewById(R.id.wish_price)
        deleteButton = view.findViewById(R.id.delete_button)
        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                presenter.deleteWish()
            }
        }
        presenter.attachView(this)

        return view
    }

    override fun getWishId(): Long {
        return requireArguments().getLong("wishId")
    }

    override fun onSuccess() {
        Toast.makeText(
            requireContext(),
            getString(R.string.wish_deletion_success),
            Toast.LENGTH_SHORT
        ).show()

        dialog?.dismiss()
    }

    override fun onFailure() {
        Toast.makeText(
            requireContext(),
            getString(R.string.wish_deletion_failure),
            Toast.LENGTH_SHORT
        ).show()

        dialog?.dismiss()
    }

    override fun setWishData(wish: Wish) {
        if (wish.imageUri.isEmpty()) {
            imageView.setImageResource(R.drawable.no_image_placeholder)
        } else {
            imageView.setImageURI(Uri.parse(wish.imageUri))
        }
        titleText.text = wish.title
        descriptionText.text = wish.description
        priceText.text = wish.price.toString()
    }

    companion object {
        fun newInstance(wishId: Long): DeleteWishFragmentDialog {
            val dialog = DeleteWishFragmentDialog()
            val bundle = Bundle()
            bundle.putLong("wishId", wishId)

            dialog.arguments = bundle

            return dialog
        }
    }
}