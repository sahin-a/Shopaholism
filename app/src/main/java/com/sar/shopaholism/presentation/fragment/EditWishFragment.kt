package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.presentation.presenter.WishEditingPresenter
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class EditWishFragment : BaseCreateWishFragment<WishEditingView, WishEditingPresenter>(),
    WishEditingView {

    override val presenter: WishEditingPresenter by inject()
    private val args: EditWishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_edit_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createButton?.icon =
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_save_changes,
                requireContext().theme
            )
        createButton?.setText(R.string.wish_apply_changes)
        createButton?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                saveClicked()
            }
        }

        presenter.attachView(this)
    }

    private suspend fun saveClicked() = coroutineScope {
        val success: Boolean = createWish(action = { title, imageUri, description, price ->
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
                        getString(R.string.wish_changes_applied),
                        Toast.LENGTH_SHORT
                    ).show()

                    navigateTo(R.id.action_editWishFragment_to_wishesOverviewFragment)
                }
                false -> Toast.makeText(
                    requireContext(),
                    getString(R.string.wish_apply_changes_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getWishId(): Long {
        return args.wishId
    }
}
