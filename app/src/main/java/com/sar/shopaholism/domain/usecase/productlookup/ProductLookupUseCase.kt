package com.sar.shopaholism.domain.usecase.productlookup

import com.sar.shopaholism.data.remote.productlookup.exceptions.FailedToRetrieveProductsException
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.exception.NoProductsFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.ProductLookupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductLookupUseCase(
    private val productLookupRepository: ProductLookupRepository,
    private val logger: Logger
) {

    suspend fun getProductsByName(name: String): List<Product> = withContext(Dispatchers.IO) {
        var products: List<Product> = emptyList()

        try {
            products = productLookupRepository.getProductsByName(name)
        } catch (e: FailedToRetrieveProductsException) {

        }

        if (products.isEmpty()) {
            logger.d(TAG, "no products found")

            throw NoProductsFoundException()
        }

        return@withContext products
    }

    suspend fun getProductByName(name: String): Product? {
        return getProductsByName(name).firstOrNull()
    }

    companion object {
        const val TAG = "ProductLookupUseCase"
    }

}