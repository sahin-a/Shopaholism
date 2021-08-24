package com.sar.shopaholism.presentation.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.presenter.WishDeletionPresenter
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class DeleteWishFragmentDialog : DialogFragment(), WishDeletionView {
    private val presenter: WishDeletionPresenter by inject()

    // Views
    private lateinit var deleteButton: Button
    private lateinit var cancelButton: FloatingActionButton

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delete_wish, container, false)

        titleTextView = view.findViewById(R.id.wish_title)
        descriptionTextView = view.findViewById(R.id.wish_description)
        priceTextView = view.findViewById(R.id.wish_price)
        deleteButton = view.findViewById(R.id.delete_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        deleteButton.setOnClickListener {
            runBlocking {
                coroutineScope {
                    var success = false
                    launch {
                        success = presenter.deleteWish(getWishId())
                    }.join()

                    var message = when (success) {
                        true -> "Wish deleted"
                        false -> "Deletion failed"
                    }

                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)

                    dialog?.dismiss()
                }
            }
        }
        cancelButton.setOnClickListener {
            dialog?.cancel()
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
                wish = presenter.getWish(getWishId())
            }.join()

            wish?.let { wish ->
                titleTextView.setText(wish.title)
                descriptionTextView.setText(wish.description)
                priceTextView.setText(wish.price.toString())
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val fragment: Fragment = requireParentFragment()

        if (fragment is DialogInterface.OnDismissListener) {
            (fragment as DialogInterface.OnDismissListener).onDismiss(dialog)
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