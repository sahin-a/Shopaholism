package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.WishesAdapter
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import com.sar.shopaholism.presentation.view.WishesOverviewView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject


class WishesOverviewFragment() : Fragment(), WishesOverviewView {
    val presenter: WishesOverviewPresenter by inject()

    private val wishes = MutableLiveData<List<Wish>>()

    // Views
    private lateinit var intermediateProgressBar: ProgressBar
    private lateinit var wishesRecyclerView: RecyclerView
    private lateinit var createWishButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishes_overview, container, false)

        setupIntermediateBar(view)
        setupRecyclerView(view)
        setupCreateButton(view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        CoroutineScope(Dispatchers.Main).launch {
            presenter.loadWishes()
        }
    }

    private fun setupIntermediateBar(view: View) {
        intermediateProgressBar = view.findViewById(R.id.indeterminate_progressbar)
    }

    private fun setupRecyclerView(view: View) {
        wishesRecyclerView = view.findViewById(R.id.wishes_recycler_view)
        val wishesAdapter = WishesAdapter(this)
        wishes.observe(
            viewLifecycleOwner,
            { wishes -> wishesAdapter.submitList(wishes.toMutableList()) })
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
}