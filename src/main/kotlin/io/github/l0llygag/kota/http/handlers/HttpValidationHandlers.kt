package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.http.HttpObject
import io.github.l0llygag.kota.http.enums.HttpMethod
import io.github.l0llygag.kota.http.enums.HttpStatus
import io.github.l0llygag.kota.http.enums.HttpVersion

/**
 * Handler for checking if HTTP Version is valid/supported.
 */
class HttpVersionHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject): HttpObject {
        if (httpObject.statusLine.httpVersion == HttpVersion.UNKNOWN) {
            error = true
            httpObject.status = HttpStatus.HTTP_VERSION_NOT_SUPPORTED
        }

        return httpObject
    }
}

/**
 * Handler for checking if HTTP Method is supported or not. (Only GET supported)
 */
class HttpMethodHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject): HttpObject {
        if (httpObject.statusLine.httpMethod == HttpMethod.UNKNOWN) {
            error = true
            httpObject.status = HttpStatus.METHOD_NOT_ALLOWED
        }

        return  httpObject
    }
}