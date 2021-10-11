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
    /*set(value: Double) {
        field = BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN)
            .toDouble()
    }*/

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
}
