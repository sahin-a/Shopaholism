package com.sar.shopaholism.data.serizaliation

import com.google.gson.Gson

class JsonDeserializer : Deserializer {
    override fun <T> deserialize(value: String, classOfT: Class<T>): T =
        Gson().fromJson(value, classOfT)
}