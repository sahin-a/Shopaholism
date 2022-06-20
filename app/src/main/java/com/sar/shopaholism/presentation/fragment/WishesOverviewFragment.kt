package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.presentation.adapter.WishesAdapter
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import com.sar.shopaholism.presentation.view.WishesOverviewView
import org.koin.android.ext.android.inject


class WishesOverviewFragment : BaseFragment<WishesOverviewPresenter, WishesOverviewView>(),
    WishesOverviewView {
    override val presenter: WishesOverviewPresenter by inject()
    private val logger: Logger by inject()
    private val wishes = MutableLiveData<List<Wish>>()

    private lateinit var emptyStateIndicatorView: View
    private lateinit var intermediateProgressBar: ProgressBar
    private lateinit var wishesRecyclerView: RecyclerView
    private lateinit var createWishButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.d("DEBUG EPIC", "onCreateView")
        return inflater.inflate(R.layout.fragment_wishes_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEmptyStateIndicator(view)
        setupIntermediateBar(view)
        setupRecyclerView(view)
        setupCreateButton(view)
        logger.d("DEBUG EPIC", "onViewCreated")
        presenter.attachView(this)
    }

    private fun setupEmptyStateIndicator(view: View) {
        emptyStateIndicatorView = view.findViewById(R.id.empty_state_indicator)
    }

    private fun setupIntermediateBar(view: View) {
        intermediateProgressBar = view.findViewById(R.id.indeterminate_progressbar)
    }

    private fun setupRecyclerView(view: View) {
        val wishesAdapter = WishesAdapter(this) {
            presenter.onWishDeleted()
        }

        wishes.observe(viewLifecycleOwner) { wishes ->
            wishesAdapter.submitList(wishes)
        }

        wishesRecyclerView = view.findViewById(R.id.wishes_recycler_view)
        wishesRecyclerView.adapter = wishesAdapter
    }

    private fun setupCreateButton(view: View) {
        createWishButton = view.findViewById(R.id.create_wish_button)
        createWishButton.setOnClickListener {
            presenter.navigateToCreateWishFragment()
        }
    }

    override fun showWishes(wishes: List<Wish>) {
        this.wishes.value = wishes
    }

    override fun navigateTo(resourceActionId: Int) {
        view?.findNavController()
            ?.navigate(resourceActionId)
    }

    override fun showLoading(visible: Boolean) {
        intermediateProgressBar.isVisible = visible
        intermediateProgressBar.isIndeterminate = visible

        enableButtons(enabled = !visible)
    }

    override fun enableButtons(enabled: Boolean) {
        createWishButton.isEnabled = enabled
    }

    override fun showEmptyState() {
        emptyStateIndicatorView.visibility = View.VISIBLE
    }
}