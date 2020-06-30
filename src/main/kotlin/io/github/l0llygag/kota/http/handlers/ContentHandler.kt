package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.enums.HttpHeader
import io.github.l0llygag.kota.enums.HttpStatus
import io.github.l0llygag.kota.enums.MimeType
import io.github.l0llygag.kota.http.HttpObject
import java.nio.file.Path

class ContentHandler : AbstractHandler() {

    override fun handle(httpObject: HttpObject): HttpObject {
        // for content negotiation [RFC 2295] and other headers dependent behaviours
        // val headersIn = request.headersIn
        val headersOut = httpObject.headersOut

        val normalizedPath = normalizePath(httpObject.path)
        val dataFile = normalizedPath.toFile()
        val dataStream = dataFile.inputStream()
        httpObject.fileSystemPath = normalizedPath
        val notFound = !dataFile.exists()

        if (notFound) {
            error = true
            httpObject.status = HttpStatus.NOT_FOUND
            return httpObject
        }

        val mimeType = determineMimeType(dataFile.name)

        var charset = ""
        if (mimeType.charset != "null")
            charset += "; charset=${mimeType.charset}"

        headersOut.appendToHeader(HttpHeader.CONTENT_TYPE, mimeType.mime + charset)
        headersOut.appendToHeader(HttpHeader.CONTENT_LENGTH, "${dataFile.length()}")

        httpObject.headersOut = headersOut
        httpObject.contentStream = dataStream

        return httpObject
    }

    private fun normalizePath(path: String): Path {
        if (path.endsWith("/"))
            return Path.of("public", path + "index.html")
        return Path.of("public", path)
    }

    private fun determineMimeType(fileName: String): MimeType {
        val regex = "(\\.\\w+\$)".toRegex()

        return regex.find(fileName)?.let { match ->
            MimeType.values().find { it.ext == match.groupValues[0] }
        } ?: MimeType.UNKNOWN
    }
}