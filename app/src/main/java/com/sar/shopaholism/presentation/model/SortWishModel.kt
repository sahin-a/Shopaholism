package com.sar.shopaholism.presentation.model

import com.sar.shopaholism.domain.entity.Wish

data class SortWishModel(
    private var mainWish: Wish? = null,
    private var otherWishes: List<Wish>? = null
)