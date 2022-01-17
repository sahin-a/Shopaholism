package com.sar.shopaholism.data.wikipedia.dto

data class SearchResultDto(
    val id: Int,
    val key: String,
    val title: String,
    val excerpt: String,
    val description: String?,
    val thumbnail: ThumbnailDto?
)
