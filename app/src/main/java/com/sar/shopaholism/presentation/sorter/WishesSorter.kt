package com.sar.shopaholism.presentation.sorter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.SelectionResult

class WishesSorter(
    private val mainWish: Wish,
    private val selectionResults: List<SelectionResult>
) {

    fun sort(): List<Wish> {
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
    }

}