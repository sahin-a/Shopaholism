package com.sar.shopaholism.data.serizaliation

interface Deserializer {
    fun <T> deserialize(value: String, classOfT: Class<T>): T
}

