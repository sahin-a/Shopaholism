package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.PreferencesDataSource

class Storages(dataSource: PreferencesDataSource) {
    val maxSearchResultStorage = BooleanStorage("maxSearchResults", dataSource)
}