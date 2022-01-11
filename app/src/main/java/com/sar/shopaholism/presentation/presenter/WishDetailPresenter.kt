package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.exception.NoProductsFoundException
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.productlookup.ProductLookupUseCase
import com.sar.shopaholism.presentation.view.WishDetailView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WishDetailPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getProductLookupUseCase: ProductLookupUseCase,
    private val logger: Logger
) : BasePresenter<WishDetailView>() {

    private fun getWishId(): Long {
        return view?.getWishId() ?: -1
    }

    suspend fun loadData() = coroutineScope {
        launch {
            var wish: Wish? = null
            var relatedProducts: List<Product> = emptyList()

            try {
                wish = getWishUseCase.execute(getWishId())
                relatedProducts = getProductLookupUseCase.getProductsByName(wish.title)
            } catch (e: WishNotFoundException) {
                logger.d(TAG, "WishNotFoundException thrown")
            } catch (e: NoProductsFoundException) {
                logger.d(TAG, "NoProductsFoundException thrown")
            }

            wish?.let {
                launch(Dispatchers.Main) {
                    view?.toggleLoadingIndicator(false)
                    showData(wish, relatedProducts)
                }
            }
        }
    }

    private fun showData(wish: Wish, relatedProducts: List<Product>) {
        view?.showData(
            wish.title,
            wish.imageUri,
            relatedProducts
        )
    }

    companion object {
        const val TAG = "WishDetailPresenter"
    }

}