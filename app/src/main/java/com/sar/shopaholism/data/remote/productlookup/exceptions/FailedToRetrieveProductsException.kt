package com.sar.shopaholism.data.remote.productlookup.exceptions

class FailedToRetrieveProductsException :
    Exception {
    constructor(message: String? = null) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}