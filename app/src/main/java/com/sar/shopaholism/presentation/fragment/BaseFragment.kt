package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sar.shopaholism.presentation.presenter.BasePresenter

abstract class BaseFragment<TPresenter : BasePresenter<TView>, TView> : Fragment() {
    protected abstract val presenter: TPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        savedInstanceState?.let { onSaveInstanceState(savedInstanceState) }
    }
}