package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sar.shopaholism.presentation.presenter.BasePresenter

abstract class BaseDialogFragment<TPresenter : BasePresenter<TView>, TView> :
    BottomSheetDialogFragment() {
    protected abstract val presenter: TPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION")
        retainInstance = true
        savedInstanceState?.let { onSaveInstanceState(savedInstanceState) }
    }
}