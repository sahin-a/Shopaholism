package com.sar.shopaholism.presentation.model

data class CreateWishModel(
    val id: Long = 0L,
    var title: String = "",
    var imageUri: String = "",
    var description: String = "",
    var price: Double = -1.0
)