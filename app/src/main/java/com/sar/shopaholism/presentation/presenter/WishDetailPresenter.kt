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
        CoroutineScope(Dispatchers.Main).launch {
            loadData()
        }
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
            view?.setWishData(it)
            setWikiPages(getWikiEntries(it.title))
        }
    }

    private fun setWikiPages(pages: List<WikiPage>) {
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