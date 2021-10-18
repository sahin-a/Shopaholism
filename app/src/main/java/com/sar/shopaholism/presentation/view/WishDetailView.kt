package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.productlookup.Product

interface WishDetailView {
    fun getWishId(): Long
    fun showData(title: String, imageUri: String, relatedProducts: List<Product>)
}