package com.sar.shopaholism.domain.repository

import com.sar.shopaholism.domain.entity.productlookup.Product

interface ProductLookupRepository {
    suspend fun getProductsByName(name: String): List<Product>
}