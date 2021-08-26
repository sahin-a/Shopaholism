package com.sar.shopaholism.domain.usecase

object WishValidation {

    /**
     * only non null parameters will be checked
     */
    fun validate(
        wishId: Long? = null,
        imageUri: String? = null,
        title: String? = null,
        description: String? = null,
        price: Double? = null,
        priority: Int? = null
    ) {
        wishId?.let {
            require(it > 0) { "Wish doesn't have a valid Id" }
        }

        imageUri?.let {
            require(it.isNotBlank()) { "Wish Image can't be blank" }
        }

        title?.let {
            require(it.isNotBlank()) { "Wish title can't be blank" }
        }

        description?.let {
            require(it.isNotBlank()) { "Wish description can't be blank" }
        }

        price?.let {
            require(it >= 0) { "Wish price can't be less than 0" }
        }

        priority?.let {
            require(it >= 0) { "Wish priority can't be less than 0" }
        }
    }
}