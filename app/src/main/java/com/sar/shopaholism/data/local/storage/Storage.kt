package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.KeyValueDataSource

abstract class Storage<T>(
    private val key: String,
    private val dataSource: KeyValueDataSource
) {
    abstract fun get(): T
    abstract fun set(key: String, value: T)
}