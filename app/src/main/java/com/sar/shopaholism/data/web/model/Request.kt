package com.sar.shopaholism.data.web.model

import com.sar.shopaholism.data.web.HttpMethod

data class Request(
    val path: String,
    val method: HttpMethod,
    val parameters: Map<String, String> = mapOf(),
    val headers: Map<String, String> = mapOf()
)