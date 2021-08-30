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

abstract class BaseCreateWishFragment<MvpView : WishCreationView, Presenter : BasePresenter<MvpView>> :
    BaseFragment(), WishCreationView {

    // Views

    protected lateinit var imageImageView: FileImageView
    protected lateinit var selectImageButton: FloatingActionButton
    protected lateinit var titleEditText: EditText
    protected lateinit var descriptionEditText: EditText
    protected lateinit var priceEditText: EditText
    protected lateinit var createButton: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true);

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                createButton.isEnabled = ctaEnabled()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        imageImageView = view.findViewById(R.id.wish_image)
        imageImageView.addImageUriChangedListener {
            createButton.isEnabled = ctaEnabled()
        }

        selectImageButton = view.findViewById(R.id.select_image_button)
        selectImageButton.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
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

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
                imageImageView.setImageURI(uri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * This will be called after its done connecting the views to their class fields in onCreateView
     */
    abstract fun onInit()

    open suspend fun createWish(action: suspend (title: String, imageUri: String, description: String, price: Double) -> Boolean): Boolean {
        return action(
            titleEditText.text.toString(),
            imageImageView.imageUri.toString(),
            descriptionEditText.text.toString(),
            priceEditText.text.toString().toDoubleOrNull() ?: -1.0
        )
    }

    open fun ctaEnabled(): Boolean {
        return titleEditText.text.isNotBlank()
                && descriptionEditText.text.isNotBlank()
                && priceEditText.text.isNotBlank()
                && imageImageView.imageUri != null
                && imageImageView.imageUri.toString().isNotBlank()
    }

    open fun navigateTo(resourceActionId: Int) {
        findNavController().navigate(resourceActionId)
    }

    override fun setData(title: String, imageUri: String, description: String, price: String) {
        titleEditText.setText(title)
        imageImageView.setImageURI(Uri.parse(imageUri))
        descriptionEditText.setText(description)
        priceEditText.setText(price)
    }

}