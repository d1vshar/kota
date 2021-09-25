package io.github.l0llygag.kota.implementations.handlers

import io.github.l0llygag.kota.core.ServerConfiguration
import io.github.l0llygag.kota.core.HttpObject
import io.github.l0llygag.kota.core.HttpHeader
import io.github.l0llygag.kota.core.HttpStatus
import io.github.l0llygag.kota.core.MimeType
import io.github.l0llygag.kota.core.handlers.AbstractHandler
import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.function.Predicate
import kotlin.io.path.isDirectory

/**
 * Handler for adding content and related headers to the response, or returning error if content not found.
 */
class ContentHandler : AbstractHandler() {

    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): Pair<Boolean, HttpObject> {
        // for content negotiation [RFC 2295] and other headers dependent behaviours
        // val headersIn = request.headersIn
        val headersOut = httpObject.headersOut

        val extractedPath = extractPathString(httpObject.path)
        val isValid = isLegalPath(extractedPath)

        if (!isValid) {
            httpObject.status = HttpStatus.BAD_REQUEST
            return false to httpObject
        }

        val normalizedPath = normalizePath(extractedPath, serverConfiguration.publicFolder)
        val dataFile = normalizedPath.toFile()

        httpObject.fileSystemPath = normalizedPath
        val notFound = !dataFile.exists()

        if (notFound) {
            httpObject.status = HttpStatus.NOT_FOUND
            return false to httpObject
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

        return true to httpObject
    }

    internal fun extractPathString(path: String): String {
        val regex = "[^?]+".toRegex()

        return URLDecoder.decode(regex.find(path)?.value ?: path, StandardCharsets.UTF_8.name())
    }

    internal fun isLegalPath(path: String): Boolean {
        val pathTokens = path.trim('/').split("/")
        var level = 0

        for (token in pathTokens) {
            if (token == "..") --level
            else ++level

            if (level < 0) return false
        }

        return true
    }

    internal fun normalizePath(path: String, publicFolder: String): Path {
        val trimmedPath = path.trim('/')
        if (Path.of(publicFolder, trimmedPath).isDirectory()) {
            return Path.of(publicFolder, trimmedPath, "index.html")
        } else {
            if (hasExtension(trimmedPath)) {
                return Path.of(publicFolder, trimmedPath)
            }
            return Path.of(publicFolder, "$trimmedPath.html")
        }
    }

    private fun hasExtension(path: String): Boolean {
        val dot = path.lastIndexOf('.');
        if (dot > 0) return true;
        return false;
    }

    internal fun determineMimeType(fileName: String): MimeType {
        val regex = "(\\.\\w+\$)".toRegex()

        return regex.find(fileName)?.let { match ->
            MimeType.values().find { it.ext == match.groupValues[0] }
        } ?: MimeType.UNKNOWN
    }
}