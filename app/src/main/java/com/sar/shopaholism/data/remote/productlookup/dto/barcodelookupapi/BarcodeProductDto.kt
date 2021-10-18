package com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi

import com.google.gson.annotations.SerializedName

data class BarcodeProductDto(
    val asin: String = "",
    val barcode_formats: String = "",
    val barcode_number: String = "",
    val brand: String = "",
    val category: String = "",
    val color: String = "",
    val description: String = "",
    val features: List<Any> = emptyList(),
    val images: List<String> = emptyList(),
    val model: String = "",
    @SerializedName("reviews") val barcodeReviewDtos: List<BarcodeReviewDto> = emptyList(),
    @SerializedName("stores") val barcodeStoreDtos: List<BarcodeStoreDto> = emptyList(),
    val title: String = ""
)