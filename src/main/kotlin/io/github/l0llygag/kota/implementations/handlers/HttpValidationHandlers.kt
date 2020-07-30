package io.github.l0llygag.kota.implementations.handlers

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
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): Pair<Boolean, HttpObject> {
        var success = true
        if (httpObject.statusLine.httpVersion == HttpVersion.UNKNOWN) {
            success = false
            httpObject.status = HttpStatus.HTTP_VERSION_NOT_SUPPORTED
        }

        return success to httpObject
    }
}

/**
 * Handler for checking if HTTP Method is supported or not. (Only GET supported)
 */
class HttpMethodHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): Pair<Boolean, HttpObject> {
        var success = true
        if (httpObject.statusLine.httpMethod == HttpMethod.UNKNOWN) {
            success = false
            httpObject.status = HttpStatus.METHOD_NOT_ALLOWED
        }

        return success to httpObject
    }
}