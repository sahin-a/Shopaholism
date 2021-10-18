package com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi

import com.google.gson.annotations.SerializedName

data class BarcodeProductsDto(
    @SerializedName("products") val barcodeProducts: List<BarcodeProductDto> = emptyList()
)