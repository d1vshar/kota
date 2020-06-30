package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.enums.HttpMethod
import io.github.l0llygag.kota.enums.HttpStatus
import io.github.l0llygag.kota.enums.HttpVersion
import java.io.FileInputStream
import java.nio.file.Path

data class HttpObject(
    var status: HttpStatus,

    // HTTP/1.1 stuff
    var httpMethod: HttpMethod = HttpMethod.GET,
    var httpVersion: HttpVersion = HttpVersion.HTTP_1_1,

    // request
    var statusLine: StatusLine,
    var contentStream: FileInputStream? = null,
    var headersIn: Headers,

    // response
    var path: String,
    var fileSystemPath: Path,
    var headersOut: Headers
)