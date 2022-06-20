package com.sar.shopaholism.domain.usecase

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateAllWishesPriorityUseCase(
    private val getWishesUseCase: GetWishesUseCase,
    private val updateWishUseCase: UpdateWishUseCase
) {

    suspend fun execute(previousWishCount: (currentWishesCount: Int) -> Int) =
        coroutineScope {
            val wishes = getWishesUseCase.execute().first()
            val wishesCount = wishes.count()

            wishes.forEach { wish ->
                launch {
                    val positiveVotes =
                        previousWishCount(wishesCount).toDouble() * (wish.priority / 100)
                    val recalculatedPriority = (positiveVotes / wishesCount) * 100

                    updateWishUseCase.execute(wish.apply {
                        priority = recalculatedPriority
                    })
                }
            }
        }
}