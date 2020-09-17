package io.github.l0llygag.kota.core.response

import io.github.l0llygag.kota.core.HttpObject
import io.github.l0llygag.kota.core.HttpVersion
import java.io.OutputStream
import java.lang.StringBuilder

/**
 * This class is responsible for writing the response to the socket, byte by byte.
 *
 * @param writer OutputStream of the connection.
 * @param httpObject HttpObject to be used for forming the response.
 */
class ResponseWriter(private val writer: OutputStream, private val httpObject: HttpObject) {

    /**
     * Get "Meta" from the `HttpObject`. Meta is the first response status line and the response headers.
     *
     * @return Byte array of the string "meta" of response
     */
    private fun getRequestMeta(): ByteArray {
        val stringBuilder = StringBuilder()

        stringBuilder.appendln("${HttpVersion.HTTP_1_1.version} ${httpObject.status.code} ${httpObject.status.description})")

        for (header in httpObject.headersOut.map)
            stringBuilder.appendln("${header.key.headersKeys}: ${header.value}")

        return stringBuilder.toString().toByteArray()
    }

    /**
     * Writes to socket. Writes body only if [io.github.l0llygag.kota.core.HttpObject.contentStream] is not null.
     * If it's null, then either no body or file not found.
     */
    fun write() {
        val metaData = getRequestMeta()
        val buf = ByteArray(8192)
        writer.write(metaData)

        httpObject.contentStream?.let {
            writer.write("\n".toByteArray())

            var c = httpObject.contentStream?.read(buf) ?: -1
            while (c != -1) {
                writer.write(buf)
                c = httpObject.contentStream?.read(buf) ?: -1
            }
        }
        httpObject.contentStream?.close()
        writer.flush()
    }
}