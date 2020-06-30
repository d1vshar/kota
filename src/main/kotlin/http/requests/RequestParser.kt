package http.requests

import enums.HttpHeader
import enums.HttpMethod
import enums.HttpStatus
import enums.HttpVersion
import http.Headers
import http.HttpObject
import http.StatusLine
import java.nio.file.Path
import java.util.*

class RequestParser(req: String) {

    private val lines = req.trim().split("\n")

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

    fun getParsedRequest(): HttpObject {
        val statusLine = getStatusLine()
        val headers = getHeaders()

        return HttpObject(
            statusLine = statusLine,
            httpMethod = HttpMethod.GET,
            httpVersion = HttpVersion.HTTP_1_1,
            path = statusLine.path,
            fileSystemPath = Path.of(statusLine.path),
            headersIn = headers,
            headersOut = Headers(EnumMap(HttpHeader::class.java)),
            status = HttpStatus.UNDECIDED
        )
    }
}