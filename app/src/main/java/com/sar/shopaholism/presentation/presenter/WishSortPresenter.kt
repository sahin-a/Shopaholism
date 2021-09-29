package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.adapter.SelectionResult
import com.sar.shopaholism.presentation.model.SortWishModel
import com.sar.shopaholism.presentation.sorter.WishesReprioritizer
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class WishSortPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getWishesUseCase: GetWishesUseCase,
    private val updateWishUseCase: UpdateWishUseCase,
    var model: SortWishModel,
    private val logger: Logger
) :
    BasePresenter<WishSortView>(),
    KoinComponent {

    private fun getMainWishId(): Long {
        return view!!.getMainWishId()
    }

    private suspend fun getMainWish(): Wish? {

        val id = getMainWishId()
        var wish: Wish? = null

        model.mainWish?.let { mainWish ->
            if (id != mainWish.id) {
                clearModel()
            }
        }

        try {
            wish = getWishUseCase.execute(wishId = id)
        } catch (e: Exception) {
            logger.d(TAG, "Failed to retrieve wish with the id: $id")
        }

        return wish
    }

    private suspend fun getOtherWishes(): List<Wish>? {
        val otherWishes: List<Wish>? = getWishesUseCase.execute()
            .catch {
                logger.d(TAG, "Failed to retrieve all wishes")
            }.firstOrNull()

        return otherWishes?.filter { otherWish -> otherWish.id != model.mainWish?.id }
    }

    suspend fun loadData() {
        this@WishSortPresenter.apply {
            model.mainWish = getMainWish()
            model.otherWishes = getOtherWishes()
        }
    }

    fun addResult(result: SelectionResult) {
        val oldResult = model.selectionResults.lastOrNull { it.otherWish == result.otherWish }

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

    fun clearModel() {
        model = SortWishModel()
    }

    suspend fun submitResult() = withContext(Dispatchers.Main) {
        launch(Dispatchers.Main) {
            if (model.selectionResults.count() != model.otherWishes?.count()) {
                return@launch
            }

            model.mainWish?.let { mainWish ->
                val reprioritizedWishes = WishesReprioritizer.sort(
                    mainWish = mainWish,
                    preferredWishes = model.selectionResults.filter { it.isPreferred }
                        .map { it.otherWish },
                    otherWishes = model.selectionResults.filter { !it.isPreferred }
                        .map { it.otherWish }
                )

                reprioritizedWishes.forEach { newWish ->
                    updateWish(newWish)
                }

                postSubmitResult()
            }
        }
    }

    private fun postSubmitResult() {
        view?.resultSubmitted()
        clearModel()
    }

    private suspend fun updateWish(wish: Wish): Boolean {
        try {
            updateWishUseCase.execute(wish)
            return true
        } catch (e: IllegalArgumentException) {
            logger.d(TAG, "Wish update failed because of invalid values")
        } catch (e: WishNotUpdatedException) {
            logger.d(TAG, "Failed to update wish")
        } catch (e: Exception) {
            logger.d(TAG, "Failed to update wish, Exception thrown")
        }

        return false
    }

    companion object {
        const val TAG = "WishSortPresenter"
    }

}