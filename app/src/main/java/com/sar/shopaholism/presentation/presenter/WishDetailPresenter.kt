package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWikiPageUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.model.WishDetailModel
import com.sar.shopaholism.presentation.view.WishDetailView
import kotlinx.coroutines.coroutineScope


class WishDetailPresenter(
    private val getWishUseCase: GetWishUseCase,
    private val getWikiPageUseCase: GetWikiPageUseCase,
    private val logger: Logger,
    private val model: WishDetailModel
) : BasePresenter<WishDetailView>() {

    private fun getWishId(): Long {
        return view?.getWishId() ?: -1
    }

    override suspend fun onNewViewAttached() {
        super.onNewViewAttached()
        //model.onWikiPagesChanged = { setWikiSearchResults(model.wikiPages) }
        loadData()
    }

    override suspend fun onAttachView() = coroutineScope {
        super.onAttachView()
        setWikiSearchResults(model.wikiPages)
    }

    private suspend fun getWish(): Wish? {
        var wish: Wish? = null
        try {
            wish = getWishUseCase.execute(getWishId())
        } catch (e: WishNotFoundException) {
            logger.d(TAG, "WishNotFoundException thrown")
        }

        return wish
    }

    private suspend fun getWikiEntries(title: String): List<WikiPage> =
        getWikiPageUseCase.execute(title, 25)

    private suspend fun loadData() {
        getWish()?.let {
            model.wish = it
            model.wikiPages = getWikiEntries(it.title)
        }
    }

    private fun setWish(wish: Wish) {
        view?.setWishData(wish)
    }

    private fun setWikiSearchResults(pages: List<WikiPage>) {
        view?.toggleLoadingIndicator(false)

        if (pages.isNullOrEmpty()) {
            view?.showError()
        } else {
            view?.setWikiPages(pages)
        }
    }

    companion object {
        const val TAG = "WishDetailPresenter"
    }
}