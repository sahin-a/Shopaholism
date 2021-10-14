package com.sar.shopaholism.data.remote.productlookup.source

import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity

interface ProductLookupDataSource {
    fun getProductsByName(name: String): List<ProductEntity>
}