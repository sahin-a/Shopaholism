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
    private val model: SortWishModel,
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

    fun addVote(vote: SelectionResult) {
        val oldVote = model.votes.lastOrNull { it.otherWish.id == vote.otherWish.id }
        when (oldVote != null) {
            true -> {
                val idx = model.votes.indexOf(oldVote)
                model.votes[idx] = vote
            }
            else -> model.votes.add(vote)
        }
    }

    fun showNextPage() {
        view?.showNextPage()
    }

    suspend fun submitVotes() = coroutineScope {
        val isVotingComplete = model.votes.count() != model.otherWishes.count()
        if (isVotingComplete) {
            return@coroutineScope
        }

        if (model.mainWish == null) {
            return@coroutineScope
        }

        val summary = VoteSummary(
            model.mainWish!!,
            model.votes.map {
                Vote(it.otherWish, it.isPreferred)
            }
        )
        updateWishPriorityByVotesUseCase.execute(summary)
        launch(Dispatchers.Main) {
            postSubmitResult()
        }
    }

    fun getVotes(): List<SelectionResult> = model.votes

    private fun postSubmitResult() {
        wishFeedbackService.wishSuccessfullyRated()
        view?.votesSubmitted()
    }

    companion object {
        const val TAG = "WishSortPresenter"
    }

}
