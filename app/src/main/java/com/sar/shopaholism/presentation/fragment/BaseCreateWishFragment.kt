package com.sar.shopaholism.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sar.shopaholism.R
import com.sar.shopaholism.presentation.presenter.BasePresenter
import com.sar.shopaholism.presentation.view.WishCreationView
import com.sar.shopaholism.presentation.widgets.FileImageView

abstract class BaseCreateWishFragment<MvpView : WishCreationView, Presenter : BasePresenter<MvpView>>
    : BaseFragment<Presenter, MvpView>(), WishCreationView {

    // Views
    protected var imageImageView: FileImageView? = null
    protected var selectImageButton: FloatingActionButton? = null
    protected var titleEditText: EditText? = null
    protected var descriptionEditText: EditText? = null
    protected var priceEditText: EditText? = null
    protected var createButton: MaterialButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                createButton?.isEnabled = ctaEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        imageImageView = view.findViewById(R.id.wish_image)
        imageImageView?.addImageUriChangedListener {
            createButton?.isEnabled = ctaEnabled()
        }

        selectImageButton = view.findViewById(R.id.select_image_button)
        selectImageButton?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(maxSize = 1024)
                .maxResultSize(width = 1080, height = 1080)
                .start()
        }

        titleEditText = view.findViewById(R.id.wish_title)
        titleEditText?.addTextChangedListener(textWatcher)

        descriptionEditText = view.findViewById(R.id.wish_description)
        descriptionEditText?.addTextChangedListener(textWatcher)

        priceEditText = view.findViewById(R.id.wish_price)
        priceEditText?.addTextChangedListener(textWatcher)

        createButton = view.findViewById(R.id.create_wish_button)
        createButton?.isEnabled = ctaEnabled()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
                imageImageView?.setImageURI(uri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(
                    requireContext(),
                    ImagePicker.getError(data),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.image_selection_cancelled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    open suspend fun createWish(action: suspend (title: String, imageUri: String, description: String, price: Double) -> Boolean): Boolean {
        val success = action(
            titleEditText?.text.toString(),
            imageImageView?.imageUri?.toString() ?: "",
            descriptionEditText?.text.toString(),
            priceEditText?.text.toString().toDoubleOrNull() ?: 0.0
        )

        return success
    }

    open fun ctaEnabled(): Boolean {
        return titleEditText?.text?.isNotBlank() ?: false
    }

    open fun navigateTo(resourceActionId: Int) {
        findNavController().navigate(resourceActionId)
    }

    override fun setData(title: String, imageUri: String, description: String, price: String) {
        titleEditText?.setText(title)
        imageImageView?.setImageURI(Uri.parse(imageUri))
        descriptionEditText?.setText(description)
        priceEditText?.setText(price)
    }

}