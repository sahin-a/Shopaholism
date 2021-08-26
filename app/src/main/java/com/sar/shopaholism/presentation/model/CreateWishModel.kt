package com.sar.shopaholism.presentation.model

data class CreateWishModel(
    var title: String = "",
    var imageUri: String = "",
    var description: String = "",
    var price: Double = -1.0
)