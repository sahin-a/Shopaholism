package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.WishesAdapter
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import com.sar.shopaholism.presentation.view.WishesOverviewView
import org.koin.android.ext.android.inject


class WishesOverviewFragment() : Fragment(), WishesOverviewView {
    val presenter: WishesOverviewPresenter by inject()

    private val wishes = MutableLiveData<List<Wish>>()

    // Views
    private lateinit var wishesRecyclerView: RecyclerView
    private lateinit var createWishButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishes_overview, container, false)

        setupRecyclerView(view)
        setupCreateButton(view)

        presenter.attachView(this)
        presenter.loadWishes()

        return view
    }

    private fun setupRecyclerView(view: View) {
        wishesRecyclerView = view.findViewById(R.id.wishes_recycler_view)
        val wishesAdapter = WishesAdapter(this)
        wishes.observe(viewLifecycleOwner, { wishes -> wishesAdapter.submitList(wishes) })
        wishesRecyclerView.adapter = wishesAdapter
    }

    private fun setupCreateButton(view: View) {
        createWishButton = view.findViewById(R.id.create_wish_button)
        createWishButton.setOnClickListener {
            presenter.createWish()
        }
    }

    override fun showWishes(wishes: List<Wish>) {
        this.wishes.value = wishes
    }

    override fun navigateTo(resourceActionId: Int) {
        view?.findNavController()
            ?.navigate(resourceActionId)
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }
}