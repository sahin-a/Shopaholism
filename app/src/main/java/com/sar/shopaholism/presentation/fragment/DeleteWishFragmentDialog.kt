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
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.presentation.presenter.WishDeletionPresenter
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class DeleteWishFragmentDialog : DialogFragment(), WishDeletionView {
    private val presenter: WishDeletionPresenter by inject()

    // Views
    private lateinit var deleteButton: Button

    private lateinit var imageImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delete_wish, container, false)

        imageImageView = view.findViewById(R.id.wish_image)
        titleTextView = view.findViewById(R.id.wish_title)
        descriptionTextView = view.findViewById(R.id.wish_description)
        priceTextView = view.findViewById(R.id.wish_price)
        deleteButton = view.findViewById(R.id.delete_button)

        deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                coroutineScope {
                    var success = presenter.deleteWish(getWishId())

                    val message = when (success) {
                        true -> getString(R.string.wish_deleted)
                        false -> getString(R.string.wish_deletion_failed)
                    }

                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }
                }
            }
        }
        setCurrentData()

        return view
    }

    private fun getWishId(): Long {
        return requireArguments().getLong("wishId")
    }

    override fun setCurrentData(): Unit = runBlocking {
        coroutineScope {
            var wish: Wish? = null

            launch {
                try {
                    wish = presenter.getWish(getWishId())
                } catch (e: WishNotFoundException) {

                } catch (e: IllegalArgumentException) {

                }
            }.join()

            wish?.let {
                if (it.imageUri.isEmpty()) {
                    imageImageView.setImageResource(R.drawable.no_image_placeholder)
                } else {
                    imageImageView.setImageURI(Uri.parse(it.imageUri))
                }
                titleTextView.text = it.title
                descriptionTextView.text = it.description
                priceTextView.text = it.price.toString()
            }
        }
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