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

class CreateWishFragment : WishCreationView,
    BaseCreateWishFragment<WishCreationView, WishCreationPresenter>() {

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
                        success = createWish(action = { title, description, price ->
                            try {
                                presenter.createWish(title, description, price)
                                return@createWish true
                            } catch (e: WishNotCreatedException) {

                            } catch (e: IllegalArgumentException) {

                            }

                            return@createWish false
                        })
                    }.join()

                    when (success) {
                        true -> {
                            Toast.makeText(
                                requireContext(),
                                "Wish created",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateTo(R.id.action_createWishFragment_to_wishesOverviewFragment)
                        }

                        false -> Toast.makeText(
                            requireContext(),
                            "Failed to create Wish",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        createButton.isEnabled = ctaEnabled()
    }
}