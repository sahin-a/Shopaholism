package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish

interface WishSortView {
    fun getMainWishId(): Long
    fun showNextPage()
    fun votesSubmitted()
    fun setData(wish: Wish, otherWishes: List<Wish>)
}