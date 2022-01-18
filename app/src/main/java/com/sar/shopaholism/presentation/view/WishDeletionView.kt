package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish

interface WishDeletionView {
    fun setWishData(wish: Wish)
    fun getWishId(): Long
    fun onSuccess()
    fun onFailure()
}