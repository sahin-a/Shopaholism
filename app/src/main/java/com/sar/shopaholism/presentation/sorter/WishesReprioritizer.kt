package com.sar.shopaholism.presentation.sorter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.SelectionResult

class WishesReprioritizer private constructor() {

    companion object {
        fun recalculatePriority(currentPriority: Int, newWishesCount: Int, wishesCount: Int): Int {
            val otherWishesCount = (wishesCount - 1)

            if (otherWishesCount <= 0) {
                throw Exception("count of other wishes can't be 0 or less")
            }

            val oldCount = otherWishesCount - newWishesCount
            val preferredCount: Double = oldCount * (currentPriority.toDouble() / 100)

            return ((preferredCount / otherWishesCount) * 100).toInt()
        }

        /*fun sort(mainWish: Wish, selectionResults: List<SelectionResult>): List<Wish> {
            // TODO: Wishes need to get reprioritized whenever a new one is created or one is deleted
            // TODO: rewrite this code in a reuseable way
            /* New Concept - COOMING SOON TM -
                - preferred and unpreferred are two seperate lists and are being passed to this func
                as parameter, this should get rid off the dependency

                - return value now is = hmm the same?

             */

            val reprioritizedWishes = mutableListOf<Wish>()
            val preferredResults = selectionResults.filter { it.isPreferred }
            val unpreferredResults = selectionResults.filter { !it.isPreferred }

            /* $ Sorting Algorithm $
            * - mainWish new priority is the percentage of how often it was preferred in total
            *
            * - if it has the same priority as a preferred one, decrease priority by 1 for each preferred
            * with the same priority
            *
            * - if it has the same priority as a non preferred one, decrease the non preferred priority
            * by 1
            * */

            val unpreferredCount = unpreferredResults.count()

            // main wish has been preferred atleast once
            if (unpreferredCount > 0) {
                mainWish.priority = ((unpreferredCount.toDouble() / selectionResults.count()) * 100)
                    .toInt()

                val penalty = 1
                // if it has the same priority as a preferred one, make sure it gets listed below
                // it's ascending so in case the next entry matches after having its priority lowered
                // we capture those cases as well
                preferredResults.sortedByDescending { it.otherWish.priority }
                    .forEach {
                        if (mainWish.priority == it.otherWish.priority && mainWish.priority > 0) {
                            mainWish.priority -= penalty
                        }
                    }

                unpreferredResults.forEach {
                    if (it.otherWish.priority > 0) {
                        it.otherWish.priority -= penalty
                        reprioritizedWishes.add(it.otherWish)
                    }
                }

            } else {
                mainWish.priority = 0
            }

            reprioritizedWishes.add(mainWish)

            return reprioritizedWishes
        }*/

        fun sort(mainWish: Wish, preferredWishes: List<Wish>, otherWishes: List<Wish>): List<Wish> {
            // TODO: Wishes need to get reprioritized whenever a new one is created or one is deleted
            // TODO: rewrite this code in a reuseable way
            /* New Concept - COOMING SOON TM -
                - preferred and unpreferred are two seperate lists and are being passed to this func
                as parameter, this should get rid off the dependency

                - return value now is = hmm the same?

             */

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