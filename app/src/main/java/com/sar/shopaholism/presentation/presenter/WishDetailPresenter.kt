package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWikiPageUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.view.WishDetailView
import kotlinx.coroutines.CoroutineScope
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

    override fun onAttachView() {
        super.onAttachView()

        CoroutineScope(Dispatchers.Default).launch {
            loadData()
        }
    }

    private suspend fun loadData() = coroutineScope {
        launch {
            var wish: Wish? = null

            try {
                wish = getWishUseCase.execute(getWishId())
            } catch (e: WishNotFoundException) {
                logger.d(TAG, "WishNotFoundException thrown")
            }

            wish?.let {
                val wikiPages: List<WikiPage> = getWikiPageUseCase.execute(it.title)

                launch(Dispatchers.Main) {
                    view?.toggleLoadingIndicator(false)
                    view?.setWishData(it)

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