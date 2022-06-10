package com.sar.shopaholism.presentation.model

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.SelectionResult

data class SortWishModel(
    var mainWish: Wish? = null,
    var otherWishes: List<Wish> = listOf(),
    val selectionResults: MutableList<SelectionResult> = mutableListOf()
)