package com.sar.shopaholism.data.local.source

interface KeyValueDataSource {
    fun getString(key: String, defaultValue: String): String
    fun getInt(key: String, defaultValue: Int): Int

    fun putString(key: String, value: String)
    fun putInt(key: String, value: Int)
}