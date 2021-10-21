package com.sar.shopaholism.data.remote.productlookup.entity

data class ProductEntity(
    val title: String,
    val images: List<String>,
    val stores: List<StoreEntity>
)
