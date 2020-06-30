package io.github.l0llygag.kota.http.response

import io.github.l0llygag.kota.http.HttpObject
import io.github.l0llygag.kota.http.enums.HttpVersion
import java.io.OutputStream

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
        var str = ""

        str += "${HttpVersion.HTTP_1_1.version} ${httpObject.status.code} ${httpObject.status.description}\n"

        for (header in httpObject.headersOut.map)
            str += "${header.key.headersKeys}: ${header.value}\n"

        return str.toByteArray()
    }

    /**
     * Writes to socket. Writes body only if [io.github.l0llygag.kota.http.HttpObject.contentStream] is not null.
     * If it's null, then either no body or file not found.
     */
    fun write() {
        val metaData = getRequestMeta()
        writer.write(metaData)

        if (httpObject.contentStream != null) {
            writer.write("\n".toByteArray())

            var c = httpObject.contentStream?.read() ?: -1
            while (c != -1) {
                writer.write(c)
                c = httpObject.contentStream?.read() ?: -1
            }
        }
        httpObject.contentStream?.close()
        writer.flush()
    }
}