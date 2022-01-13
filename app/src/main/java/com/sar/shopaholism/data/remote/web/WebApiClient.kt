package com.sar.shopaholism.data.remote.web

import com.sar.shopaholism.data.remote.web.model.Request
import com.sar.shopaholism.data.serizaliation.Deserializer

interface WebApiClient {
    suspend fun sendRequest(request: Request): String
    suspend fun <T> sendRequest(request: Request, deserializer: Deserializer, classOfT: Class<T>): T

    fun setBasePath(basePath: String)
    fun setBaseParams(baseParams: Map<String, String>)
}