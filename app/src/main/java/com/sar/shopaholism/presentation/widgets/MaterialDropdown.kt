package com.sar.shopaholism.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.sar.shopaholism.R


class MaterialDropdown(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    private var elements: MutableList<Pair<String, Any>> = mutableListOf()
    private var textInputLayout: TextInputLayout
    private var textView: TextView
    private var autoCompleteTextView: AutoCompleteTextView
    private var text
        get() = textView.text
        set(value) {
            textView.text = value
        }

    init {
        val view = inflate(context, R.layout.material_dropdown, this)
        textView = view.findViewById(R.id.text_view)
        textInputLayout = view.findViewById(R.id.dropdown_menu)
        autoCompleteTextView = textInputLayout.editText as AutoCompleteTextView

        val attrsSet = intArrayOf(
            android.R.attr.text
        )
        context.obtainStyledAttributes(attrs, attrsSet).apply {
            text = getString(0)
        }.recycle()
    }

    /**
     * @param Pair.First this value gets used as label
     * @param Pair.Second object
     */
    fun setItems(elements: MutableList<Pair<String, Any>>) {
        this.elements = elements
        updateAdapter()
    }

    private fun updateAdapter() {
        val items = elements.map { it.first }
        val adapter = ArrayAdapter(context, R.layout.dropdown_item, items)
        autoCompleteTextView.setAdapter(adapter)
    }

    fun selectItem(item: Pair<String, Any>): Boolean {
        val itemExists = elements.any { it == item }
        if (itemExists) {
            autoCompleteTextView.setText(item.first, false)
        }

        return itemExists
    }

    fun getSelectedItem(): Pair<String, Any>? {
        val text = autoCompleteTextView.text
        return elements.firstOrNull { it.first == text.toString() }
    }
}