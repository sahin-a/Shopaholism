package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.SortWishModel
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent

class WishSortPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getWishesUseCase: GetWishesUseCase,
    private var model: SortWishModel,
    private val logger: Logger
) :
    BasePresenter<WishSortView>(),
    KoinComponent {

    var mainWish: Wish? = null
        private set

    var otherWishes: List<Wish>? = null
        private set

    private fun getMainWishId(): Long {
        return view!!.getMainWishId()
    }

    private suspend fun getMainWish(): Wish? {
        val id = getMainWishId()
        var wish: Wish? = null

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

        return otherWishes?.filter { otherWish -> otherWish.id != mainWish?.id }
    }

    suspend fun loadData() {
        this@WishSortPresenter.apply {
            this.mainWish = getMainWish()
            this.otherWishes = getOtherWishes()
        }
    }

    fun clearModel() {
        model = SortWishModel()
    }

    companion object {
        const val TAG = "WishSortPresenter"
    }

}