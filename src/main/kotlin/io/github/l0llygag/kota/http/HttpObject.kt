package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.http.enums.HttpStatus
import io.github.l0llygag.kota.http.enums.HttpVersion
import java.io.FileInputStream
import java.nio.file.Path

/**
 * Main data class of the server. It contains information related to request and response.
 * This object is first formed by [io.github.l0llygag.kota.http.requests.RequestParser] and then passed down all the
 * handlers.
 *
 * @param statusLine This is status line of the request.
 * @param headersIn This is the headers received in the request.
 * @param path This is the path received in the request.
 * @param status This is the status of the response.
 * @param httpVersion This is HTTP version used for response. Default = HTTP/1.1
 * @param fileSystemPath This is [java.nio.file.Path] of the string [io.github.l0llygag.kota.http.HttpObject.path] received.
 * @param contentStream This is the [java.io.FileInputStream] of response file. Is set to null if not found (404).
 * @param headersOut This is the headers sent in the request.
 */
data class HttpObject(
    // request
    var statusLine: StatusLine,
    var headersIn: Headers,
    var path: String,

    // response
    var status: HttpStatus,
    var httpVersion: HttpVersion = HttpVersion.HTTP_1_1,
    var fileSystemPath: Path,
    var contentStream: FileInputStream? = null,
    var headersOut: Headers
)