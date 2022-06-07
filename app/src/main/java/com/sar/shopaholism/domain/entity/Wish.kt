package com.sar.shopaholism.domain.entity

import java.math.BigDecimal
import java.math.RoundingMode

class Wish {

    var id: Long
        private set
    var imageUri: String
        private set
    var title: String
        private set
    var description: String
        private set
    var priority: Double
    var price: Double

    constructor(
        id: Long,
        imageUri: String,
        title: String,
        description: String,
        price: Double,
        priority: Double
    ) {
        this.id = id
        this.imageUri = imageUri
        this.title = title
        this.description = description
        this.price = price
        this.priority = priority
    }

    private fun roundValue(value: Double, scale: Int): BigDecimal {
        return BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN)
    }

    fun getRoundedPrice(scale: Int = 2) = roundValue(price, scale).toDouble()

    fun getRoundedPriority(scale: Int = 2) = roundValue(priority, scale).toDouble()

    override fun equals(other: Any?): Boolean {
        return other is Wish
                && other.id == id
                && other.price == price
                && other.priority == priority
                && other.description == description
                && other.imageUri == imageUri
                && other.title == title
    }
}
