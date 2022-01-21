package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.PreferencesDataSource

abstract class Storage<T>(
    private val key: String,
    private val dataSource: PreferencesDataSource
) {
    open fun get(): T {
        return dataSource.getValue(key)
    }

    open fun set(key: String, value: T) {
        dataSource.setValue(key, value)
    }
}