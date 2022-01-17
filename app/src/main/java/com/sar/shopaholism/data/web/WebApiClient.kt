package com.sar.shopaholism.data.web

import com.sar.shopaholism.data.web.model.Request
import com.sar.shopaholism.data.serizaliation.Deserializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface WebApiClient {
    suspend fun sendRequest(
        request: Request,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): String

    suspend fun <T> sendRequest(
        request: Request,
        deserializer: Deserializer,
        classOfT: Class<T>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): T

    fun setBasePath(basePath: String)
    fun setBaseParams(baseParams: Map<String, String>)
}