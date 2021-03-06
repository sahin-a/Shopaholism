package com.sar.shopaholism.presentation.widgets

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet

class FileImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    var imageUriChangedListeners = mutableListOf<(imageUri: Uri?) -> Unit>()
    var imageUri: Uri? = null

    private fun notifyImageUriChangedListeners() {
        for (listener in imageUriChangedListeners) {
            listener(imageUri)
        }
    }

    fun addImageUriChangedListener(listener: (imageUri: Uri?) -> Unit) {
        imageUriChangedListeners.add(listener)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)

        imageUri = uri
        notifyImageUriChangedListeners()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putString("imageUri", imageUri.toString())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val bundle = state
            setImageURI(Uri.parse(bundle.getString("imageUri")))
            super.onRestoreInstanceState(bundle.getParcelable("superState"))
            return
        }

        super.onRestoreInstanceState(state)
    }

}
