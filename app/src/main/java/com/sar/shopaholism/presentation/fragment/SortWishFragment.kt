package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.adapter.WishSortViewPagerAdapter
import com.sar.shopaholism.presentation.presenter.WishSortPresenter
import com.sar.shopaholism.presentation.view.WishSortView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

// TODO: Rename parameter arguments, choose names that match
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

        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)

        presenter.attachView(this@SortWishFragment)

        runBlocking {
            launch(Dispatchers.IO) {
                presenter.loadData()

                presenter.mainWish?.let { mainWish ->
                    presenter.otherWishes?.let { otherWishes ->

                        viewPager.adapter = WishSortViewPagerAdapter(
                            mainWish = mainWish,
                            otherWishes = otherWishes
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
        // TODO: Rename and change types and number of parameters
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
}