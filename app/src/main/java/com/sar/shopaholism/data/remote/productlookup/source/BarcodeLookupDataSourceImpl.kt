package com.sar.shopaholism.data.remote.productlookup.source

import com.sar.shopaholism.data.remote.productlookup.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity
import com.sar.shopaholism.data.remote.productlookup.mapper.toProductEntities

class BarcodeLookupDataSourceImpl(
    private val apiClient: BarcodeLookupApi
) : ProductLookupDataSource {

    override fun getProductsByName(name: String): List<ProductEntity> {
        val productsDto = apiClient.getProductsByName(name)

        return productsDto.barcodeProducts.toProductEntities()
    }

}