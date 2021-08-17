package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish

interface WishesOverviewView {
    fun showLoading()
    fun showWishes(wishes: List<Wish>)
}