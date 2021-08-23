package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.presentation.presenter.WishEditingPresenter
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class EditWishFragment : WishEditingView,
    BaseCreateWishFragment<WishEditingView, WishEditingPresenter>() {

    private val presenter: WishEditingPresenter by inject()

    private val args: EditWishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        presenter.attachView(this)
        return inflater.inflate(R.layout.fragment_edit_wish, container, false)
    }

    private fun setCurrentData() = runBlocking {
        coroutineScope {
            var oldWish: Wish? = null
            launch {
                oldWish = presenter.getOriginalWish(args.wishId)
            }.join()

            oldWish?.let {
                titleEditText.setText(it.title)
                descriptionEditText.setText(it.description)
                priceEditText.setText(it.price.toString())
            }
        }
    }

    override fun onInit() {
        setCurrentData()

        createButton.icon =
            resources.getDrawable(R.drawable.ic_save_changes, requireContext().theme)
        createButton.setText(R.string.wish_apply_changes)
        createButton.setOnClickListener {
            runBlocking {
                coroutineScope {
                    var success: Boolean = false

                    launch {
                        success = createWish(action = { title, description, price ->
                            try {
                                presenter.updateWish(args.wishId, title, description, price)
                                return@createWish true
                            } catch (e: WishNotUpdatedException) {

                            } catch (e: IllegalArgumentException) {

                            }

                            return@createWish false
                        })
                    }.join()

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
    }
}