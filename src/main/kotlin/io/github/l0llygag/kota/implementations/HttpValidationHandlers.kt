package io.github.l0llygag.kota.implementations

import io.github.l0llygag.kota.core.ServerConfiguration
import io.github.l0llygag.kota.core.HttpObject
import io.github.l0llygag.kota.core.HttpMethod
import io.github.l0llygag.kota.core.HttpStatus
import io.github.l0llygag.kota.core.HttpVersion
import io.github.l0llygag.kota.core.handlers.AbstractHandler

/**
 * Handler for checking if HTTP Version is valid/supported.
 */
class HttpVersionHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): HttpObject {
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
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): HttpObject {
        if (httpObject.statusLine.httpMethod == HttpMethod.UNKNOWN) {
            error = true
            httpObject.status = HttpStatus.METHOD_NOT_ALLOWED
        }

        return  httpObject
    }
}