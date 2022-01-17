package com.sar.shopaholism.data.wikipedia.exceptions

class WikiPageSearchFailedException : Exception {
    constructor(message: String? = null) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}