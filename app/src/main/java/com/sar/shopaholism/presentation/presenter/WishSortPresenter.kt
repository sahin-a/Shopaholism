package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.*
import com.sar.shopaholism.presentation.adapter.SelectionResult
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.SortWishModel
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WishSortPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getWishesUseCase: GetWishesUseCase,
    private val updateWishPriorityByVotesUseCase: UpdateWishPriorityByVotesUseCase,
    var model: SortWishModel,
    private val logger: Logger,
    private val wishFeedbackService: WishFeedbackService
) : BasePresenter<WishSortView>() {

    override suspend fun onNewViewAttached() {
        super.onNewViewAttached()
        loadData()
    }

    override suspend fun onAttachView() {
        super.onAttachView()
        onDataLoaded()
    }

    private fun getMainWishId(): Long {
        return view!!.getMainWishId()
    }

    private suspend fun getMainWish(): Wish? {
        val id = getMainWishId()
        var wish: Wish? = null

        try {
            wish = getWishUseCase.execute(id)
        } catch (e: Exception) {
            logger.d(TAG, "Failed to retrieve wish with the id: $id")
        }

        return wish
    }

    private suspend fun getOtherWishes(): List<Wish> {
        val otherWishes: List<Wish> = getWishesUseCase.execute()
            .catch {
                logger.d(TAG, "Failed to retrieve all wishes")
            }.firstOrNull() ?: listOf()

        return otherWishes.filter { otherWish -> otherWish.id != model.mainWish?.id }
    }

    private suspend fun loadData() {
        model.mainWish = getMainWish()
        model.otherWishes = getOtherWishes()
    }

    private fun onDataLoaded() {
        model.mainWish?.let { wish ->
            view?.setData(wish, model.otherWishes)
        }
    }

    fun addResult(result: SelectionResult) {
        val oldResult = model.selectionResults.lastOrNull { it.otherWish.id == result.otherWish.id }

        when (oldResult != null) {
            true -> {
                val idx = model.selectionResults.indexOf(oldResult)
                model.selectionResults[idx] = result

                logger.d(
                    tag = TAG,
                    message = "SelectionResult replaced"
                )
            }
            else -> {
                model.selectionResults.add(result)

                logger.d(
                    tag = TAG,
                    message = "SelectionResult added"
                )
            }
        }
    }

    fun showNextPage() {
        view?.showNextPage()
    }

    suspend fun submitResult() = coroutineScope {
        val isVotingComplete = model.selectionResults.count() != model.otherWishes.count()
        if (isVotingComplete) {
            return@coroutineScope
        }

        if (model.mainWish == null) {
            return@coroutineScope
        }

        val summary = VoteSummary(
            model.mainWish!!,
            model.selectionResults.map {
                Vote(it.otherWish, it.isPreferred)
            }
        )
        updateWishPriorityByVotesUseCase.execute(summary)
        launch(Dispatchers.Main) {
            postSubmitResult()
        }
    }

    private fun postSubmitResult() {
        wishFeedbackService.wishSuccessfullyRated()
        view?.resultSubmitted()
    }

    companion object {
        const val TAG = "WishSortPresenter"
    }

}
