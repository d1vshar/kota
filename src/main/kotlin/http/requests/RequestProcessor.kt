package http.requests

import enums.*
import http.Header
import http.ErrorDocument
import http.ResponseDocument
import java.io.File
import java.nio.file.Path

class RequestProcessor(private val request: ParsedRequest) {

    var responseDocument: ResponseDocument? = null
    var errorDocument: ErrorDocument? = null

    private val validateHttpMethod = validateHttpMethod()
    private val validateHttpVersion = validateHttpVersion()
    private val validatePath = validatePath()
    private val initInHeaders = initInHeaders()

    private fun validateHttpMethod(): HttpMethod? {
        return HttpMethod.values().find { it.name == request.requestLine.method }
    }

    private fun validateHttpVersion(): HttpVersion? {
        return HttpVersion.values().find {
            println("${it.string} | ${request.requestLine.version} | ${it.string == request.requestLine.version}")
            it.string == request.requestLine.version
        }
    }

    private fun validatePath(): Path? {
        val file = File("public/"+request.requestLine.target)
        if (file.exists()) return file.toPath()
        return null
    }

    private fun initInHeaders(): List<Header> {
        val headers = arrayListOf<Header>()
        for (pair in request.header) {
            headers.add(Header(pair.first, pair.second))
        }

        return headers
    }

    private fun fetchData(): String {
        validatePath?.let {
            return validatePath.toFile().readText()
        }
        return ""
    }

    fun validate(): Boolean {
        when {
            validateHttpVersion == null -> {
                errorDocument = ErrorDocument(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, HttpVersion.HTTP_1_1)
            }
            validateHttpMethod == null -> {
                errorDocument = ErrorDocument(HttpStatus.METHOD_NOT_ALLOWED, validateHttpVersion)
            }
            validatePath == null -> {
                errorDocument = ErrorDocument(HttpStatus.NOT_FOUND, validateHttpVersion)
            }
        }

        if (errorDocument == null) {
            val data = fetchData()
            val outHeaders = arrayListOf<Header>()
            outHeaders.add(Header("Content-Length", data.length.toString()))
            outHeaders.add(Header("Content-Type", "text/html; charset=utf-8"))
            responseDocument = ResponseDocument(HttpStatus.OK, HttpVersion.HTTP_1_1, initInHeaders, outHeaders, data)
        }

        return errorDocument == null
    }
}