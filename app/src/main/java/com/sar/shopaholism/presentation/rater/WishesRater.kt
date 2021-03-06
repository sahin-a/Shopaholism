package com.sar.shopaholism.presentation.rater

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WishesRater(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishesUseCase: GetWishesUseCase
) {

    /**
     * recalculates the rating of all wishes and updates it in the db
     */
    suspend fun recalculateAndUpdateRatings(oldWishesCount: (wishesCount: Int) -> Int) =
        coroutineScope {
            val wishes = getWishesUseCase.execute().first()
            val wishesCount = wishes.count()

            launch {
                wishes.filter { it.priority > 0 }.forEach { wish ->
                    launch {
                        val newPriority = recalculatePriority(
                            currentPriority = wish.priority,
                            oldWishesCount = oldWishesCount(wishesCount),
                            wishesCount = wishesCount
                        )

                        wish.priority = newPriority
                        updateWishUseCase.execute(wish)
                    }
                }
            }
        }

    companion object {
        /**
         * recalculates the priority by finding out how many times it was preferred based on the
         * count of new entries added
         *
         * @param currentPriority current priority of the wish
         * @param oldWishesCount previous total of all wishes
         * @param wishesCount current total of all wishes
         * @return priority
         */
        fun recalculatePriority(
            currentPriority: Double,
            oldWishesCount: Int,
            wishesCount: Int

        ): Double {
            val preferredCount: Double = oldWishesCount * (currentPriority / 100)

            return ((preferredCount / wishesCount) * 100)
        }

        /**
         * recalculates the priority of the main wish based on how many times it was preferred in
         * comparison to the total amount of choices
         *
         * @param mainWish the wish that all the other ones are being compared to
         * @param preferredWishes list of wishes that are preferred over the main wish
         * @param otherWishes list of wishes that weren't preferred over the main wish
         *
         * @return list of wishes that had their priority changed
         */
        fun rateWish(
            mainWish: Wish,
            preferredWishes: List<Wish>,
            otherWishes: List<Wish>
        ): List<Wish> {
            val reprioritizedWishes = mutableListOf<Wish>()

            /* $ Sorting Algorithm $
            * - mainWish new priority is the percentage of how often it was preferred in total
            *
            * - if it has the same priority as a preferred one, decrease priority by 1 for each preferred
            * with the same priority
            *
            * - if it has the same priority as a non preferred one, decrease the non preferred priority
            * by 1
            * */

            val mainWishPreferredCount: Int = otherWishes.count()

            if (mainWishPreferredCount > 0) {
                val totalWishes = preferredWishes.count() + otherWishes.count()
                mainWish.priority = ((mainWishPreferredCount.toDouble() / totalWishes) * 100)

            } else {
                mainWish.priority = 0.0
            }

            reprioritizedWishes.add(mainWish)

            return reprioritizedWishes
        }
    }

}