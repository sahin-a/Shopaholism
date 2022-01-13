package com.sar.shopaholism.data.remote.productlookup.source

import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity
import com.sar.shopaholism.data.remote.productlookup.mapper.toProductEntities

class BarcodeLookupDataSourceImpl(
    private val apiClient: BarcodeLookupApi
) : ProductLookupDataSource {

    override suspend fun getProductsByName(name: String): List<ProductEntity> {
        val productsDto = apiClient.getProductsByName(name)

        return productsDto.barcodeProducts.toProductEntities()
    }

}