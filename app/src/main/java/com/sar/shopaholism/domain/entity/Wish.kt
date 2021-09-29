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
    var priority: Int
    var price: Double
        set(value: Double) {
            field = BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN)
                .toDouble()
        }

    constructor(
        id: Long,
        imageUri: String,
        title: String,
        description: String,
        price: Double,
        priority: Int
    ) {
        this.id = id
        this.imageUri = imageUri
        this.title = title
        this.description = description
        this.price = price
        this.priority = priority
    }
}
