package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.adapter.WishSortViewPagerAdapter
import com.sar.shopaholism.presentation.presenter.WishSortPresenter
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SortWishFragment : BaseFragment<WishSortPresenter, WishSortView>(), WishSortView {

    override val presenter: WishSortPresenter by inject()
    private var wishId: Long? = null

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            wishId = it.getLong(ARG_WISH_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sort_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.view_pager)
        presenter.attachView(this)
    }

    override fun setData(wish: Wish, otherWishes: List<Wish>) {
        viewPager.adapter = WishSortViewPagerAdapter(
            results = presenter.getVotes(),
            mainWish = wish,
            otherWishes = otherWishes
        ) {
            presenter.addVote(it)
            presenter.showNextPage()
        }
    }

    override fun getMainWishId(): Long {
        return wishId ?: -1
    }

    override fun showNextPage() {
        viewPager.adapter?.let { adapter ->
            val nextIndex = viewPager.currentItem + 1

            when (nextIndex <= (adapter.itemCount - 1)) {
                true -> viewPager.setCurrentItem(nextIndex, true)
                else -> CoroutineScope(Dispatchers.Default).launch {
                    presenter.submitVotes()
                }
            }
        }
    }

    override fun votesSubmitted() {
        findNavController().navigate(R.id.action_sortWishFragment_to_wishesOverviewFragment)
    }

    companion object {
        private const val ARG_WISH_ID = "wishId"

        @JvmStatic
        fun newInstance(wishId: Long) =
            SortWishFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_WISH_ID, wishId)
                }
            }
    }
}
