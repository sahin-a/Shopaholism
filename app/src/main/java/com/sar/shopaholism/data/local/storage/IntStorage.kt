package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.KeyValueDataSource

open class IntStorage(
    private val key: String,
    private val dataSource: KeyValueDataSource,
    private val defaultValue: Int
) : Storage<Int>(key, dataSource) {

    override fun get() = dataSource.getInt(key, defaultValue)

    override fun set(key: String, value: Int) = dataSource.putInt(key, value)
}
