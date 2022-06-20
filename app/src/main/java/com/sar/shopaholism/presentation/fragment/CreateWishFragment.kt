package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.presenter.WishCreationPresenter
import com.sar.shopaholism.presentation.view.WishCreationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CreateWishFragment : BaseCreateWishFragment<WishCreationView, WishCreationPresenter>() {

    override val presenter: WishCreationPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_create_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createButton?.setOnClickListener {
            createButton?.isEnabled = false

            CoroutineScope(Dispatchers.Default).launch {
                createClicked()
            }
        }
        createButton?.isEnabled = ctaEnabled()

        presenter.attachView(this)
    }

    private suspend fun createClicked() = coroutineScope {
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