package com.sar.shopaholism.domain.usecase.productlookup

import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.exception.NoProductsFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.ProductLookupRepository

class ProductLookupUseCase(
    private val productLookupRepository: ProductLookupRepository,
    private val logger: Logger
) {

    fun getProductsByName(name: String): List<Product> {
        var products: List<Product> = emptyList()

        try {
            products = productLookupRepository.getProductsByName(name)
        } catch (e: Exception) {
            val message = "ProductLookupRequest failed"
            logger.d(TAG, message)
        }

        if (products.isEmpty()) {
            logger.d(TAG, "no products found")

            throw NoProductsFoundException()
        }

        return products
    }

    fun getProductByName(name: String): Product? {
        return getProductsByName(name).firstOrNull()
    }

    companion object {
        const val TAG = "ProductLookupUseCase"
    }

}