package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.R
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WishesOverviewPresenter(
    private val getWishesUseCase: GetWishesUseCase,
    private val model: WishesModel,
    private val logger: Logger
) : BasePresenter<WishesOverviewView>() {

    override suspend fun onAttachView() {
        super.onAttachView()
        loadData()
    }

    private fun onDataLoaded() {
        view?.showLoading(visible = false)
        showData()
    }

    private suspend fun loadData() {
        onLoadData()
        loadWishes()
        onDataLoaded()
    }

    private fun onLoadData() {
        view?.showLoading(visible = true)
    }

    private suspend fun loadWishes() {
        val wishes = getWishesUseCase.execute()
            .catch { e -> logger.e(TAG, e.message ?: "") }
            .first()

        model.wishes = wishes.toMutableList()
        logger.i(TAG, message = "Retrieved ${wishes.size} Wishes from the local Db")
    }


    private fun showData() {
        view?.let { view ->
            if (model.wishes.isEmpty()) {
                view.showEmptyState()
            } else {
                view.showWishes(model.wishes)
            }
        }
    }

    fun onWishDeleted() {
        CoroutineScope(Dispatchers.Main).launch {
            loadData()
        }
    }

    fun navigateToCreateWishFragment() {
        view?.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)
        logger.i(tag = this::class.java.name, message = "Navigated to createWishFragment")
    }

    companion object {
        private const val TAG: String = "WishesOverviewPresenter"
    }
}
