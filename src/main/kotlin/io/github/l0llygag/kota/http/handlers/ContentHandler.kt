package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.ServerConfiguration
import io.github.l0llygag.kota.http.HttpObject
import io.github.l0llygag.kota.http.enums.HttpHeader
import io.github.l0llygag.kota.http.enums.HttpStatus
import io.github.l0llygag.kota.http.enums.MimeType
import java.nio.file.Path

/**
 * Handler for adding content and related headers to the response, or returning error if content not found.
 */
class ContentHandler : AbstractHandler() {

    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): HttpObject {
        // for content negotiation [RFC 2295] and other headers dependent behaviours
        // val headersIn = request.headersIn
        val headersOut = httpObject.headersOut

        val normalizedPath = normalizePath(httpObject.path, serverConfiguration.publicFolder)
        val dataFile = normalizedPath.toFile()
        httpObject.fileSystemPath = normalizedPath
        val notFound = !dataFile.exists()

        if (notFound) {
            error = true
            httpObject.status = HttpStatus.NOT_FOUND
            return httpObject
        }

        val dataStream = dataFile.inputStream()
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

    private fun normalizePath(path: String, publicFolder: String): Path {
        if (path.endsWith("/"))
            return Path.of(publicFolder, path + "index.html")
        return Path.of(publicFolder, path)
    }

    private fun determineMimeType(fileName: String): MimeType {
        val regex = "(\\.\\w+\$)".toRegex()

        return regex.find(fileName)?.let { match ->
            MimeType.values().find { it.ext == match.groupValues[0] }
        } ?: MimeType.UNKNOWN
    }
}