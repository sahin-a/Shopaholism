package com.sar.shopaholism.data.remote.productlookup.mapper

import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeProductDto
import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeStoreDto
import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity
import com.sar.shopaholism.data.remote.productlookup.entity.StoreEntity

/* StoreDto -> StoreEntity */
private fun map(barcodeStoreDto: BarcodeStoreDto) =
    StoreEntity(
        name = barcodeStoreDto.name,
        url = barcodeStoreDto.link,
        price = barcodeStoreDto.price,
        currency = barcodeStoreDto.currency,
        country = barcodeStoreDto.country
    )

fun BarcodeStoreDto.toStoreEntity() = map(this)
fun List<BarcodeStoreDto>.toStoreEntities() = map { it.toStoreEntity() }

/* ProductDto -> ProductEntity */
private fun map(barcodeProductDto: BarcodeProductDto) =
    ProductEntity(
        title = barcodeProductDto.title,
        stores = barcodeProductDto.barcodeStoreDtos.toStoreEntities()
    )

fun List<BarcodeProductDto>.toProductEntities() = map { it.toProductEntity() }
fun BarcodeProductDto.toProductEntity() = map(this)