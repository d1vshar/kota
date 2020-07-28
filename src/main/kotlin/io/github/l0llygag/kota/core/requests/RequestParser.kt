package io.github.l0llygag.kota.core.requests

import io.github.l0llygag.kota.core.Headers
import io.github.l0llygag.kota.core.HttpObject
import io.github.l0llygag.kota.core.StatusLine
import io.github.l0llygag.kota.core.HttpHeader
import io.github.l0llygag.kota.core.HttpMethod
import io.github.l0llygag.kota.core.HttpStatus
import io.github.l0llygag.kota.core.HttpVersion
import java.nio.file.Path
import java.util.*

/**
 * This class is used to form the [io.github.l0llygag.kota.core.HttpObject] from raw string request.
 *
 * @param req The string req received from client.
 */
class RequestParser(req: String) {

    private val lines = req.trim().split("\n")

    /**
     * Gets headers by matching against regex.
     *
     * @return [io.github.l0llygag.kota.core.Headers] object.
     */
    private fun getHeaders(): Headers {
        val regex = "(.*):\\s*(.*)".toRegex()
        val headers = Headers(EnumMap(HttpHeader::class.java))

        for (line in lines) {
            if (line.isEmpty()) break

            val matchResult = regex.matchEntire(line)
            matchResult?.let {
                val groupValues = it.groupValues

                HttpHeader.values().find { httpHeader -> httpHeader.headersKeys == groupValues[1] }?.let { findResult ->
                    headers.appendToHeader(findResult, groupValues[2])
                }
            }
        }

        return headers
    }

    /**
     * Gets status line by matching first line of request against regex.
     *
     * @return [io.github.l0llygag.kota.core.StatusLine] object.
     */
    private fun getStatusLine(): StatusLine {
        var method = ""
        var target = ""
        var version = ""

        val regex = "(\\w*)\\s(.*)\\s(HTTP/\\d\\.\\d)".toRegex()

        if (lines.isNotEmpty()) {
            val matchResult = regex.matchEntire(lines.first())
            matchResult?.let {
                method = it.groupValues[1]
                target = it.groupValues[2]
                version = it.groupValues[3]
            }
        }

        // method validation
        val httpMethod = HttpMethod.values().find { it.name == method.trim() } ?: HttpMethod.UNKNOWN

        // version validation
        val httpVersion = HttpVersion.values().find { it.version == version.trim() } ?: HttpVersion.UNKNOWN

        return StatusLine(httpMethod, target, httpVersion)
    }

    /**
     * Executed the parser.
     *
     * @return [io.github.l0llygag.kota.core.HttpObject] which will be passed to handlers.
     */
    fun getParsedRequest(): HttpObject {
        val statusLine = getStatusLine()
        val headers = getHeaders()

        return HttpObject(
            statusLine = statusLine,
            httpVersion = HttpVersion.HTTP_1_1,
            path = statusLine.path,
            fileSystemPath = Path.of(statusLine.path),
            headersIn = headers,
            headersOut = Headers(EnumMap(HttpHeader::class.java)),
            status = HttpStatus.UNDECIDED
        )
    }
}