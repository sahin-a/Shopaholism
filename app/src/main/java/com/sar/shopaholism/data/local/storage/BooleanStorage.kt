package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.PreferencesDataSource

class BooleanStorage(
    private val key: String,
    private val dataSource: PreferencesDataSource
) : Storage<Boolean>(key, dataSource) {
    override fun get() = dataSource.getValue<Boolean>(key)

    override fun set(key: String, value: Boolean) = dataSource.setValue(key, value)
}