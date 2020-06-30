package http.handlers

import enums.HttpMethod
import enums.HttpStatus
import enums.HttpVersion
import http.HttpObject

class HttpVersionHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject): HttpObject {
        if (httpObject.statusLine.httpVersion == HttpVersion.UNKNOWN) {
            error = true
            httpObject.status = HttpStatus.HTTP_VERSION_NOT_SUPPORTED
        }

        return httpObject
    }
}

class HttpMethodHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject): HttpObject {
        if (httpObject.statusLine.httpMethod == HttpMethod.UNKNOWN) {
            error = true
            httpObject.status = HttpStatus.METHOD_NOT_ALLOWED
        }

        return  httpObject
    }
}