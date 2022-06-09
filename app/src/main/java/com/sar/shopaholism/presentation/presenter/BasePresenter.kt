package com.sar.shopaholism.presentation.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BasePresenter<TView> {
    var view: TView? = null
        private set

    fun attachView(view: TView) {
        val previousView = this.view
        this.view = view

        CoroutineScope(Dispatchers.Main).launch {
            if (previousView == null || previousView != view) {
                onNewViewAttached()
            }
            onAttachView()
        }
    }

    open suspend fun onNewViewAttached() {

    }

    open suspend fun onAttachView() {

    }
}