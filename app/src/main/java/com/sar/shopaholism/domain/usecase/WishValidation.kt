package com.sar.shopaholism.domain.usecase

object WishValidation {

    /**
     * only non null parameters will be checked
     */
    fun validate(
        wishId: Long? = null,
        title: String? = null,
        price: Double? = null,
        priority: Int? = null
    ) {
        wishId?.let {
            require(it > 0) { "Wish doesn't have a valid Id" }
        }

        title?.let {
            require(it.isNotBlank()) { "Wish title can't be blank" }
        }

        price?.let {
            require(it >= 0.0) { "Wish price can't be less than 0" }
        }

        priority?.let {
            require(it >= 0) { "Wish priority can't be less than 0" }
            require(it <= 100) { "Wish priority can't be greater than 100" }
        }
    }
}