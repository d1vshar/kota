package io.github.l0llygag.kota.implementations.handlers

import io.github.l0llygag.kota.core.ServerConfiguration
import io.github.l0llygag.kota.core.HttpObject
import io.github.l0llygag.kota.core.HttpHeader
import io.github.l0llygag.kota.core.handlers.AbstractHandler
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Handler for adding date header to the response. Format as specified in RFC7231.
 *
 * This header should generally be executed at the last of the whole server pipeline
 * to maintain highest approximation of response creation time.
 */
class ResponseDateHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): Pair<Boolean, HttpObject> {
        val str = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            .withZone(ZoneId.of("GMT")).format(Instant.now())

        val headersOut = httpObject.headersOut

        headersOut.appendToHeader(HttpHeader.DATE, str);
        httpObject.headersOut = headersOut

        return true to httpObject
    }
}