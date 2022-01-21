package com.sar.shopaholism.data.local.source

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesDataSource(private val sharedPreferences: SharedPreferences) {
    fun <T> getValue(key: String): T {
        val value = sharedPreferences.getString(key, "")
        return value as T
    }

    fun <T> setValue(key: String, value: T) {
        val bytes = value

        sharedPreferences.edit {
            putString(key, value.toString())
            commit()
        }
    }
}