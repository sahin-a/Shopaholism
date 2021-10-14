package com.sar.shopaholism.domain.usecase.productlookup

import com.sar.shopaholism.domain.entity.productlookup.Product
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
            val message = "Failed to receive products by name"
            logger.d(TAG, message)
        }

        if (products.isEmpty()) {
            val message = "no products found"
            logger.d(TAG, message)

            throw Exception(message)
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