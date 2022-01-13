package com.sar.shopaholism.data.remote.web.model

import java.net.HttpURLConnection.HTTP_OK

class Response(val statusCode: Int, val content: String) {
    val isSuccessful get() = statusCode == HTTP_OK
}