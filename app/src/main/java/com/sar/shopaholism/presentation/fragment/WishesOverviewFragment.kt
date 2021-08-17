package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var createWishButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wishes_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishesRecyclerView = view.rootView.findViewById(R.id.wishes_recycler_view)
        val wishesAdapter = WishesAdapter()
        wishes.observe(viewLifecycleOwner, { wishes -> wishesAdapter.submitList(wishes) })
        wishesRecyclerView.adapter = wishesAdapter

        presenter.attachView(this)
        presenter.loadWishes()

        createWishButton = view.rootView.findViewById(R.id.create_wish_button)
        createWishButton.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_wishesOverviewFragment_to_createWishFragment)
        }
    }

    override fun showWishes(wishes: List<Wish>) {
        this.wishes.value = wishes
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }
}