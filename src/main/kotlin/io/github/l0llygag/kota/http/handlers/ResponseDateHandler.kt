package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.ServerConfiguration
import io.github.l0llygag.kota.http.HttpObject
import io.github.l0llygag.kota.http.enums.HttpHeader
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class ResponseDateHandler : AbstractHandler() {
    override fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): HttpObject {
        val str = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            .withZone(ZoneId.of("GMT")).format(Instant.now())

        val headersOut = httpObject.headersOut

        headersOut.appendToHeader(HttpHeader.DATE, str);
        httpObject.headersOut = headersOut

        return httpObject
    }

}