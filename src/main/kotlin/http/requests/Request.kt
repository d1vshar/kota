package http.requests

import enums.HttpMethod
import http.Header
import java.nio.file.Path

data class Request(
    // HTTP/1.1 stuff
    val httpMethod: HttpMethod,

    // actual data requested
    var path: Path,

    // mime headers
    var headersIn: List<Header>,
    var headersOut: List<Header>
)