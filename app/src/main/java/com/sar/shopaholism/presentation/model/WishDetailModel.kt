package com.sar.shopaholism.presentation.model

import com.sar.shopaholism.domain.entity.WikiPage
import com.sar.shopaholism.domain.entity.Wish

class WishDetailModel {
    var wish: Wish? = null
    var wikiPages: List<WikiPage> = listOf()
}
