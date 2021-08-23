package com.sar.shopaholism.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.presenter.BasePresenter

abstract class BaseCreateWishFragment<MvpView, Presenter : BasePresenter<MvpView>>(

) : BaseFragment() {
    // Views
    protected lateinit var titleEditText: EditText
    protected lateinit var descriptionEditText: EditText
    protected lateinit var priceEditText: EditText
    protected lateinit var createButton: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                createButton.isEnabled = ctaEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        titleEditText = view.findViewById(R.id.wish_title)
        titleEditText.addTextChangedListener(textWatcher)

        descriptionEditText = view.findViewById(R.id.wish_description)
        descriptionEditText.addTextChangedListener(textWatcher)

        priceEditText = view.findViewById(R.id.wish_price)
        priceEditText.addTextChangedListener(textWatcher)

        createButton = view.findViewById(R.id.create_wish_button)

        createButton.isEnabled = ctaEnabled()

        onInit()
    }

    /**
     * This will be called after its done connecting the views to their class fields in onCreateView
     */
    abstract fun onInit()

    open suspend fun createWish(action: suspend (title: String, description: String, price: Double) -> Boolean): Boolean {
        return action(
            titleEditText.text.toString(),
            descriptionEditText.text.toString(),
            priceEditText.text.toString().toDoubleOrNull() ?: 0.0
        )
    }

    open fun ctaEnabled(): Boolean {
        return titleEditText.text.isNotBlank()
                && descriptionEditText.text.isNotBlank()
                && priceEditText.text.isNotBlank()
    }

    open fun navigateTo(resourceActionId: Int) {
        findNavController().navigate(resourceActionId)
    }
}