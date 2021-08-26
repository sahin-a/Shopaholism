package com.sar.shopaholism.domain.entity

data class Wish(
    val id: Long,
    val imageUri: String,
    val title: String,
    val description: String,
    val price: Double,
    val priority: Int
)
