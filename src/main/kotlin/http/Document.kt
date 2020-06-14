package http

import enums.HttpStatus
import enums.HttpVersion

abstract class Document {
    abstract val status: HttpStatus
    abstract val httpVersion: HttpVersion
    abstract val inHeaders: List<Header>
    abstract val outHeaders: List<Header>
    abstract val body: String
    abstract val denied: Boolean

    fun generateResponse(): String {
        val statusLine = "${httpVersion.string} ${status.error} ${status.description}\n"

        var headerFields = ""
        for (header in outHeaders) {
            headerFields += "${header.name}: ${header.value}\n"
        }
        headerFields += "\n"

        return statusLine + headerFields + body
    }
}

class ResponseDocument(
    override var status: HttpStatus,
    override var httpVersion: HttpVersion,
    override var inHeaders: List<Header> = arrayListOf(),
    override var outHeaders: List<Header> = arrayListOf(),
    override var body: String = ""
) : Document() {
    override val denied: Boolean = false
}


class ErrorDocument(
    override val status: HttpStatus,
    override val httpVersion: HttpVersion,
    override val inHeaders: List<Header> = arrayListOf(),
    override val outHeaders: List<Header> = arrayListOf(),
    override val body: String = ""
) : Document() {
    override val denied: Boolean = true
}