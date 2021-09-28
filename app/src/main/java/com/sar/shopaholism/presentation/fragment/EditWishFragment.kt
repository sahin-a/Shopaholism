package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.presentation.presenter.WishEditingPresenter
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class EditWishFragment : BaseCreateWishFragment<WishEditingView, WishEditingPresenter>(),
    WishEditingView {

    private val presenter: WishEditingPresenter by inject()
    private val args: EditWishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            presenter.wishId = null
        }

        presenter.attachView(this)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_wish, container, false)
    }

    override fun postInit() {
        createButton?.icon =
            resources.getDrawable(R.drawable.ic_save_changes, requireContext().theme)
        createButton?.setText(R.string.wish_apply_changes)
        createButton?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var success: Boolean = false

                success = createWish(action = { title, imageUri, description, price ->
                    try {
                        presenter.updateWish(
                            id = args.wishId,
                            title = title,
                            imageUri = imageUri,
                            description = description,
                            price = price
                        )
                        return@createWish true
                    } catch (e: WishNotUpdatedException) {

                    } catch (e: IllegalArgumentException) {

                    }

                    return@createWish false
                })

                launch(Dispatchers.Main) {
                    when (success) {
                        true -> {
                            Toast.makeText(
                                requireContext(),
                                "Wish changes applied",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateTo(R.id.action_editWishFragment_to_wishesOverviewFragment)
                        }

                        false -> Toast.makeText(
                            requireContext(),
                            "Failed to apply changes to wish",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        presenter.getData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        presenter.updateModelData(
            title = titleEditText?.text.toString(),
            imageUri = imageImageView?.imageUri.toString(),
            description = descriptionEditText?.text.toString(),
            price = priceEditText?.text.toString().toDoubleOrNull()
        )
    }

    override fun getWishId(): Long {
        return args.wishId
    }

}