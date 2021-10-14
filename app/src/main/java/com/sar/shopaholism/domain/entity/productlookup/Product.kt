package com.sar.shopaholism.domain.entity.productlookup

data class Product(
    val title: String,
    val stores: List<Store>
)