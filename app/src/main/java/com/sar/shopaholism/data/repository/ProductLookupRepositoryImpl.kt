package com.sar.shopaholism.data.repository

import com.sar.shopaholism.data.remote.productlookup.source.ProductLookupDataSource
import com.sar.shopaholism.data.remote.productlookup.mapper.toProducts
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.repository.ProductLookupRepository

class ProductLookupRepositoryImpl(
    private val dataSource: ProductLookupDataSource
) : ProductLookupRepository {

    override suspend fun getProductsByName(name: String): List<Product> =
        dataSource.getProductsByName(name).toProducts()

}