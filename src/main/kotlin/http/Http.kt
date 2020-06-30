package http

import enums.HttpHeader
import enums.HttpMethod
import enums.HttpVersion
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap

data class Headers(val map: EnumMap<HttpHeader, String>) {
    fun appendToHeader(key: HttpHeader, value: String) {
        val v = map[key]

        if (v == null)
            map[key] = value
        else
            map[key] = "$v, $value"
    }

    fun getHeaderValue(key: HttpHeader): String? {
        return map[key]
    }
}

data class StatusLine(val httpMethod: HttpMethod, val path: String, val httpVersion: HttpVersion)