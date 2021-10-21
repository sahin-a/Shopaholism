package com.sar.shopaholism.data.remote.productlookup.mapper

import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity
import com.sar.shopaholism.data.remote.productlookup.entity.StoreEntity
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.entity.productlookup.Store

/* StoreEntity -> Store (Domain) */
private fun map(storeEntity: StoreEntity): Store =
    Store(
        name = storeEntity.name,
        url = storeEntity.url,
        price = storeEntity.price,
        country = storeEntity.country,
        currency = storeEntity.currency
    )

fun StoreEntity.toStore() = map(this)
fun List<StoreEntity>.toStores() = map { it.toStore() }

/* ProductEntity -> Product (Domain) */
private fun map(productEntity: ProductEntity): Product =
    Product(
        title = productEntity.title,
        stores = productEntity.stores.toStores(),
        images = productEntity.images
    )

fun ProductEntity.toProduct() = map(this)
fun List<ProductEntity>.toProducts() = map { it.toProduct() }