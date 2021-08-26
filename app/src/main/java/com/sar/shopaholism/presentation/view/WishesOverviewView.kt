package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish

interface WishesOverviewView {
    fun showLoading(visible: Boolean)
    fun showWishes(wishes: List<Wish>)
    fun navigateTo(resourceActionId: Int)
    fun enableButtons(enabled: Boolean)
}