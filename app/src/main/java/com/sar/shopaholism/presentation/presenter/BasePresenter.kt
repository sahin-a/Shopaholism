package com.sar.shopaholism.presentation.presenter

abstract class BasePresenter<View> {
    var view: View? = null
        private set

    fun attachView(view: View) {
        this.view = view
        onAttachView()
    }

    open fun onAttachView() {

    }
}