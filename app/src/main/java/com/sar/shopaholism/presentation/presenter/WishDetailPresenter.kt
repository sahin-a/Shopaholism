package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWikiPageUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.view.WishDetailView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WishDetailPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getWikiPageUseCase: GetWikiPageUseCase,
    private val logger: Logger
) : BasePresenter<WishDetailView>() {

    private fun getWishId(): Long {
        return view?.getWishId() ?: -1
    }

    suspend fun loadData() = coroutineScope {
        launch {
            var wish: Wish? = null

            try {
                wish = getWishUseCase.execute(getWishId())
            } catch (e: WishNotFoundException) {
                logger.d(TAG, "WishNotFoundException thrown")
            }

            wish?.let {
                var wikiPages: List<WikiPage>? = null
                try {
                    wikiPages = getWikiPageUseCase.execute(wish.title)
                } catch (e: Exception) {
                    logger.d(TAG, "Couldn't retrieve wiki pages")
                }

                launch(Dispatchers.Main) {
                    view?.toggleLoadingIndicator(false)
                    view?.setWishData(wish)

                    if (wikiPages.isNullOrEmpty()) {
                        view?.showError()
                    } else {
                        view?.setWikiPages(wikiPages)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "WishDetailPresenter"
    }

}