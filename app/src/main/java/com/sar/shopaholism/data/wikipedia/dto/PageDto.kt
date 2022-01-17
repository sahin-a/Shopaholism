package com.sar.shopaholism.data.wikipedia.dto

data class PageDto(
    val id: Int,
    val key: String,
    val title: String,
    val latest: LatestDto,
    val content_model: String,
    val license: Map<String, String>,
    val html_url: String
)