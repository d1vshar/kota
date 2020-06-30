package http.response

import enums.HttpVersion
import http.HttpObject
import java.io.OutputStream

class ResponseWriter(private val writer: OutputStream, private val httpObject: HttpObject) {

    private fun getRequestMeta(): ByteArray {
        var str = ""

        str += "${HttpVersion.HTTP_1_1.version} ${httpObject.status.code} ${httpObject.status.description}\n"

        for (header in httpObject.headersOut.map)
            str += "${header.key.headersKeys}: ${header.value}\n"

        return str.toByteArray()
    }

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