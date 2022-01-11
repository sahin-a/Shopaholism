package com.sar.shopaholism.domain.repository

import com.sar.shopaholism.domain.entity.productlookup.Product

interface ProductLookupRepository {
    fun getProductsByName(name: String): List<Product>
}