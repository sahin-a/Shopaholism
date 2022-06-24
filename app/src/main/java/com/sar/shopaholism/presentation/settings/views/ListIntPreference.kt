package com.sar.shopaholism.presentation.settings.views

import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference

class ListIntPreference : ListPreference {
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun persistString(value: String): Boolean {
        return super.persistInt(value.toInt())
    }

    override fun getPersistedString(defaultReturnValue: String?): String {
        val defaultValue = if (defaultReturnValue.isNullOrEmpty()) 0 else defaultReturnValue.toInt()
        return super.getPersistedInt(defaultValue).toString()
    }
}