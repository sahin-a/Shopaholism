package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishCreationView

open class BaseWishCreationPresenter<View : WishCreationView> : BasePresenter<View>() {

    val model = CreateWishModel()

    fun updateModelData(
        title: String? = null,
        imageUri: String? = null,
        description: String? = null,
        price: Double? = null
    ) {
        if (title != null) {
            model.title = title
        }

        if (imageUri != null) {
            model.imageUri = imageUri
        }

        if (description != null) {
            model.description = description
        }

        price?.let { model.price = it }
    }

    fun getData() {
        model.apply {
            view?.setData(
                title = title,
                imageUri = imageUri,
                description = description,
                price = when (price < 0.0) {
                    true -> ""
                    else -> price.toString()
                }
            )
        }
    }
}