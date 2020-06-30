package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.enums.HttpHeader
import io.github.l0llygag.kota.enums.HttpMethod
import io.github.l0llygag.kota.enums.HttpVersion
import java.util.*

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