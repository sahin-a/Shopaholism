package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.presentation.presenter.WishCreationPresenter
import com.sar.shopaholism.presentation.view.WishCreationView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class CreateWishFragment : BaseCreateWishFragment<WishCreationView, WishCreationPresenter>() {

    val presenter: WishCreationPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_wish, container, false)
    }

    override fun onInit() {
        presenter.attachView(this)

        createButton.setOnClickListener {
            runBlocking {
                coroutineScope {
                    var success: Boolean = false

                    launch {
                        success = createWish(
                            action = { title, imageUri, description, price ->
                            try {
                                presenter.createWish(
                                    title = title,
                                    imageUri = imageUri,
                                    description = description,
                                    price = price
                                )
                                return@createWish true
                            } catch (e: WishNotCreatedException) {

                            } catch (e: IllegalArgumentException) {

                            }

                            return@createWish false
                        })
                    }.join()

                    when (success) {
                        true -> {
                            // TODO: Clear fields


                            Toast.makeText(
                                requireContext(),
                                getString(R.string.wish_created_toast),
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateTo(R.id.action_createWishFragment_to_wishesOverviewFragment)
                        }

                        false -> Toast.makeText(
                            requireContext(),
                            getString(R.string.wish_creation_failed_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        presenter.getData() // sets data from model to view
        createButton.isEnabled = ctaEnabled()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.updateModelData(
            title = titleEditText.text.toString(),
            imageUri = imageImageView.imageUri.toString(),
            description = descriptionEditText.text.toString(),
            price = priceEditText.text.toString().toDoubleOrNull()
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.updateModelData(
            title = "",
            description = "",
            imageUri = "",
            price = -1.0
        )
    }

}