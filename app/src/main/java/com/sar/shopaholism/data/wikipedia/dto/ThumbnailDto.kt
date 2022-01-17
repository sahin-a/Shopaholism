package com.sar.shopaholism.data.wikipedia.dto

data class ThumbnailDto(
    val mimetype: String,
    val size: Int?,
    val width: Int?,
    val height: Int?,
    val duration: Int?,
    var url: String
)