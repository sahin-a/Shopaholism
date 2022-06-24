package com.sar.shopaholism.data.local.source

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesDataSource(private val sharedPreferences: SharedPreferences) : KeyValueDataSource {
    override fun getString(key: String, defaultValue: String): String =
        sharedPreferences.getString(key, defaultValue)!!

    override fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun putString(key: String, value: String) = sharedPreferences.edit {
        putString(key, value)
        commit()
    }

    override fun putInt(key: String, value: Int) = sharedPreferences.edit {
        putInt(key, value)
        commit()
    }

}