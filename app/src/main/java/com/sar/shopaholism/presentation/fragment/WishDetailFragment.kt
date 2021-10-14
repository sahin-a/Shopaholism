package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sar.shopaholism.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_WISH_ID = "wishId"

/**
 * A simple [Fragment] subclass.
 * Use the [WishDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var wishId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wishId = it.getString(ARG_WISH_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param wishId Parameter Wish Id
         * @return A new instance of fragment WishDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(wishId: String) =
            WishDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WISH_ID, wishId)
                }
            }
    }
}