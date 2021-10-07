package com.sar.shopaholism.presentation.sorter

import com.sar.shopaholism.domain.entity.Wish

class WishesReprioritizer private constructor() {

    companion object {
        /**
         * recalculates the priority by finding out how many times it was preferred based on the
         * count of new entries added
         *
         * @param currentPriority current priority of the wish
         * @param newWishesCount count of new wishes added
         * @param wishesCount current count of all wishes
         * @return priority
         */
        fun recalculatePriority(currentPriority: Int, newWishesCount: Int, wishesCount: Int): Int {
            // TODO: Wishes need to get reprioritized whenever a new one is created or one is deleted

            val otherWishesCount = (wishesCount - 1)

            if (otherWishesCount <= 0) {
                throw Exception("count of other wishes can't be 0 or less")
            }

            val oldCount = otherWishesCount - newWishesCount
            val preferredCount: Double = oldCount * (currentPriority.toDouble() / 100)

            return ((preferredCount / otherWishesCount) * 100)
                .toInt()
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
        fun sort(mainWish: Wish, preferredWishes: List<Wish>, otherWishes: List<Wish>): List<Wish> {
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

            val mainWishPreferredCount = otherWishes.count()

            // main wish has been preferred atleast once
            if (mainWishPreferredCount > 0) {
                val totalWishes = preferredWishes.count() + otherWishes.count()
                mainWish.priority = ((mainWishPreferredCount.toDouble() / totalWishes) * 100)
                    .toInt()

                // if it has the same priority as a preferred one, make sure it gets listed below
                // it's ascending so in case the next entry matches after having its priority lowered
                // we capture those cases as well

                val penalty = 1

                preferredWishes.sortedByDescending { it.priority }
                    .forEach {
                        if (mainWish.priority == it.priority && mainWish.priority > 0) {
                            mainWish.priority -= penalty
                        }
                    }

                otherWishes.forEach {
                    if (it.priority > 0) {
                        it.priority -= penalty
                        reprioritizedWishes.add(it)
                    }
                }

            } else {
                mainWish.priority = 0
            }

            reprioritizedWishes.add(mainWish)

            return reprioritizedWishes
        }
    }

}