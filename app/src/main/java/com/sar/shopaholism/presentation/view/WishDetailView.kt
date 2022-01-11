package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.entity.productlookup.Product

interface WishDetailView {
    fun getWishId(): Long
    fun showData(wish: Wish, relatedProducts: List<Product>)
    fun toggleLoadingIndicator(show: Boolean)
    fun showError()
}