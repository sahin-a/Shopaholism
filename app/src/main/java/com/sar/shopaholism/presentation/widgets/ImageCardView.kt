package com.sar.shopaholism.presentation.widgets

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.sar.shopaholism.R

class ImageCardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val imageView: FileImageView
    private val otherContentLayout: LinearLayout

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_card_view, this, true)

        imageView = view.findViewById(R.id.image_view)
        otherContentLayout = view.findViewById(R.id.other_content_layout)

        val imageResId = attrs.getAttributeResourceValue(
            "http://schemas.android.com/apk/res/android",
            "src",
            0
        )
        setImage(imageResId)
    }

    private fun showImageView(showImage: Boolean) {
        imageView.visibility = if (showImage) View.VISIBLE else View.GONE
    }

    fun setImage(resId: Int) {
        imageView.setImageResource(resId)

        if (resId == Resources.ID_NULL) {
            showImageView(showImage = false)
        } else {
            showImageView(showImage = true)
        }
    }

    fun setImage(uri: Uri?) {
        imageView.setImageURI(uri)

        if (uri == null) {
            showImageView(showImage = false)
        } else {
            showImageView(showImage = true)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        var v: View?
        while (getChildAt(1).also { v = it } != null) {
            removeView(v)
            otherContentLayout.addView(v)
        }
    }
}