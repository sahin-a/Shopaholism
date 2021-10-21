package com.sar.shopaholism.domain.entity.productlookup

data class Product(
    val title: String,
    val images: List<String>,
    val stores: List<Store>
)