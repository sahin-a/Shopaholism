package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish

interface WishSortView {
    fun getMainWishId(): Long
    fun showNextPage()
    fun resultSubmitted()
    fun setData(wish: Wish, otherWishes: List<Wish>)
}