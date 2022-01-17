package com.sar.shopaholism.presentation.view

import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish

interface WishDetailView {
    fun getWishId(): Long
    fun setWishData(wish: Wish)
    fun setWikiPages(wikiPages: List<WikiPage>)
    fun toggleLoadingIndicator(show: Boolean)
    fun showError()
}