package http

import enums.HttpStatus
import http.handlers.AbstractHandler
import http.handlers.ContentHandler
import http.handlers.HttpMethodHandler
import http.handlers.HttpVersionHandler
import http.requests.RequestParser
import http.response.ResponseWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket
import java.time.Instant

class ServerChild(private val clientSocket: Socket): Runnable {

    override fun run() {
        println("handling request from ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}")
        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val writer = clientSocket.getOutputStream()
        val startTime = Instant.now()

        var tmpLine: String?
        var request = ""

        outer@ do {
            tmpLine = reader.readLine()

            if (!tmpLine.isNullOrEmpty()) {
                request += tmpLine + "\n"
                inner@ while (true) {
                    tmpLine = reader.readLine()

                    if (tmpLine.isNullOrEmpty()) {
                        request.trim()
                        println("request = $request")
                        respond(request,writer)
                        break@inner
                    }

                    request += tmpLine + "\n"
                }
            }

            request = ""
            if (Instant.now().minusSeconds(30) >= startTime) {
                writer.close()
                clientSocket.close()
                break@outer
            }
        } while (true)

        println("timeout on ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}")
    }

    private fun respond(req: String, writer: OutputStream) {
        // handlers need to be decided programmatically using strategies depending upon request
        val handlers = arrayOf(HttpVersionHandler(), HttpMethodHandler(), ContentHandler())

        val httpObject = RequestParser(req).getParsedRequest()
        val handledHttpObject = executeAllHandlers(httpObject, handlers)

        ResponseWriter(writer, handledHttpObject).write()
    }

    private fun executeAllHandlers(httpObject: HttpObject, handlers: Array<AbstractHandler>): HttpObject {
        var tempHttpObject = httpObject

        for (handler in handlers) {
            tempHttpObject = handler.handle(tempHttpObject)
            if (handler.error) break
        }

        // If the status is still undecided, it passed through all error checkers
        if (tempHttpObject.status == HttpStatus.UNDECIDED)
            tempHttpObject.status = HttpStatus.OK

        return tempHttpObject
    }

}