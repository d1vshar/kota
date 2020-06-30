package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.http.enums.HttpHeader
import io.github.l0llygag.kota.http.enums.HttpMethod
import io.github.l0llygag.kota.http.enums.HttpVersion
import java.util.*

/**
 * Data class to store headers
 *
 * @param map starting [java.util.EnumMap] for headers.
 */
data class Headers(val map: EnumMap<HttpHeader, String>) {

    /**
     * Function to append value to a header if it exists in the map, else add it to map.
     *
     * @param key [io.github.l0llygag.kota.http.enums.HttpHeader] enum key.
     * @param value String value to be set or appended.
     */
    fun appendToHeader(key: HttpHeader, value: String) {
        val v = map[key]

        if (v == null)
            map[key] = value
        else
            map[key] = "$v, $value"
    }
}

/**
 * Data class to store status line of a request.
 *
 * @param httpMethod HTTP Method sent in request.
 * @param path HTTP path sent in request.
 * @param httpVersion HTTP version sent in request.
 */
data class StatusLine(val httpMethod: HttpMethod, val path: String, val httpVersion: HttpVersion)