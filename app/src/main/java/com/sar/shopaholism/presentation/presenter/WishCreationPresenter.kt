package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.sorter.WishesReprioritizer
import com.sar.shopaholism.presentation.view.WishCreationView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase,
    private val getWishesUseCase: GetWishesUseCase,
    private val updateWishUseCase: UpdateWishUseCase,
    private val logger: Logger
) : BaseWishCreationPresenter<WishCreationView>() {

    suspend fun createWish(
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )

            // TODO: shit doesn't work, fix soon //repriotize all wishes due to change in total wish count
            /*launch {
                reprioritizeAll()
            }*/

            return@withContext true
        } catch (e: WishNotCreatedException) {

        } catch (e: IllegalArgumentException) {

        }

        return@withContext false
    }

    private suspend fun reprioritizeAll() = withContext(Dispatchers.IO) {
        val updateAll = { wishes: List<Wish> ->
            val wishesCount = wishes.count()

            wishes.filter { it.priority > 0 }.forEach { wish ->
                val newPriority = WishesReprioritizer.recalculatePriority(
                    currentPriority = wish.priority,
                    newWishesCount = 1,
                    wishesCount = wishesCount
                )

                CoroutineScope(Dispatchers.IO).launch {
                    updatePriority(
                        wish = wish,
                        priority = newPriority
                    )
                }
            }
        }

        getWishesUseCase.execute().collect { wishes ->
            if (wishes.count() > 1) {
                updateAll(wishes)
            }
        }
    }

    private suspend fun updatePriority(wish: Wish, priority: Int) = withContext(Dispatchers.IO) {
        wish.priority = priority
        updateWishUseCase.execute(wish)
    }

    companion object {
        const val TAG = "WishCreationPresenter"
    }

}