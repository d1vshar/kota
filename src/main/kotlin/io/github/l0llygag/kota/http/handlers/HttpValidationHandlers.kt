package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.enums.HttpMethod
import io.github.l0llygag.kota.enums.HttpStatus
import io.github.l0llygag.kota.enums.HttpVersion
import io.github.l0llygag.kota.http.HttpObject

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