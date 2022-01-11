package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.presenter.WishCreationPresenter
import com.sar.shopaholism.presentation.view.WishCreationView
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class CreateWishFragment : BaseCreateWishFragment<WishCreationView, WishCreationPresenter>() {

    val presenter: WishCreationPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState == null) {
            presenter.updateModelData(
                title = "",
                imageUri = "",
                description = "",
                price = -1.0
            )
        }

        return inflater.inflate(R.layout.fragment_create_wish, container, false)
    }

    override fun postInit() {
        presenter.attachView(this)

        createButton?.setOnClickListener {
            createButton?.isEnabled = false

            CoroutineScope(Dispatchers.Default).launch {
                val success = createWish(
                    action = { title, imageUri, description, price ->
                        return@createWish presenter.createWish(
                            title = title,
                            imageUri = imageUri,
                            description = description,
                            price = price
                        )
                    })

                launch(Dispatchers.Main) {
                    when (success) {
                        true -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.wish_created_toast),
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateTo(R.id.action_createWishFragment_to_wishesOverviewFragment)
                        }

                        false -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.wish_creation_failed_toast),
                                Toast.LENGTH_SHORT
                            ).show()

                            createButton?.isEnabled = ctaEnabled()
                        }
                    }
                }
            }
        }

        presenter.getData() // sets data from model to view
        createButton?.isEnabled = ctaEnabled()
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

}