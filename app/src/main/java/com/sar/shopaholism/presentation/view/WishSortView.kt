package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.presentation.adapter.SelectionResult

interface WishSortView {
    fun getMainWishId(): Long
    fun showNextPage()
    fun resultSubmitted()
}