package com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi

data class BarcodeStoreDto(
    val availability: String,
    val condition: String,
    val country: String,
    val currency: String,
    val currency_symbol: String,
    val item_group_id: String,
    val last_update: String,
    val link: String,
    val name: String,
    val price: String,
    val sale_price: String,
    val shipping: String,
    val tax: String
)