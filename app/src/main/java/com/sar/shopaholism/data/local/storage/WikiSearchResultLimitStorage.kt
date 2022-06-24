package com.sar.shopaholism.data.local.storage

import com.sar.shopaholism.data.local.source.PreferencesDataSource
import com.sar.shopaholism.domain.settings.Settings

class WikiSearchResultLimitStorage(dataSource: PreferencesDataSource) :
    IntStorage(Settings.MAX_WIKI_SEARCH_RESULTS, dataSource, defaultValue = 10)