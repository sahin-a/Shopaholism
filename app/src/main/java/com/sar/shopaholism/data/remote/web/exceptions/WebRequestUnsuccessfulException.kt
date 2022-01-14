package com.sar.shopaholism.data.remote.web.exceptions

class WebRequestUnsuccessfulException : Exception {
    constructor(message: String? = null) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}