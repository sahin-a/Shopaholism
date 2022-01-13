package com.sar.shopaholism.data.remote.web.model

import com.sar.shopaholism.data.remote.web.HttpMethod

data class Request(
    val path: String,
    val method: HttpMethod,
    val parameters: Map<String, String>
)