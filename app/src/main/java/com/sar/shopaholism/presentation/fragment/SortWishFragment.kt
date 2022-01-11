package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.adapter.WishSortViewPagerAdapter
import com.sar.shopaholism.presentation.presenter.WishSortPresenter
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_WISH_ID = "wishId"

/**
 * A simple [Fragment] subclass.
 * Use the [SortWishFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SortWishFragment : Fragment(), WishSortView {

    private val presenter: WishSortPresenter by inject()
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sort_wish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.view_pager)

        presenter.attachView(this@SortWishFragment)

        runBlocking {
            launch(Dispatchers.IO) {
                presenter.loadData()

                presenter.model.mainWish?.let { mainWish ->
                    presenter.model.otherWishes?.let { otherWishes ->

                        viewPager.adapter = WishSortViewPagerAdapter(
                            mainWish = mainWish,
                            otherWishes = otherWishes,
                            presenter = presenter
                        )
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param wishId Id of the wish.
         * @return A new instance of fragment SortWishFragment.
         */
        @JvmStatic
        fun newInstance(wishId: Long) =
            SortWishFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_WISH_ID, wishId)
                }
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
                    presenter.submitResult()
                }
            }
        }
    }

    override fun resultSubmitted() {
        findNavController().navigate(R.id.action_sortWishFragment_to_wishesOverviewFragment)
    }
}