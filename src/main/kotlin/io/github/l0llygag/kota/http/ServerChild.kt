package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.enums.HttpStatus
import io.github.l0llygag.kota.http.handlers.AbstractHandler
import io.github.l0llygag.kota.http.handlers.ContentHandler
import io.github.l0llygag.kota.http.handlers.HttpMethodHandler
import io.github.l0llygag.kota.http.handlers.HttpVersionHandler
import io.github.l0llygag.kota.http.requests.RequestParser
import io.github.l0llygag.kota.http.response.ResponseWriter
import mu.KotlinLogging
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.Socket
import java.time.Instant

class ServerChild(private val clientSocket: Socket): Runnable {

    private val logger = KotlinLogging.logger {  }

    override fun run() {
        logger.info { "handling request from ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}" }

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
                        request = request.trim()
                        logger.trace {
                            "request from ${clientSocket.inetAddress.hostAddress}:${clientSocket.port} = $request"
                        }
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

    }

    private fun respond(req: String, writer: OutputStream) {
        // handlers need to be decided programmatically using strategies depending upon request
        val handlers = arrayOf(HttpVersionHandler(), HttpMethodHandler(), ContentHandler())

        val httpObject = RequestParser(req).getParsedRequest()

        logger.info {
            "request status line for ${clientSocket.inetAddress.hostAddress}:${clientSocket.port} " +
                "= ${httpObject.statusLine.httpMethod} ${httpObject.statusLine.path} ${httpObject.statusLine.httpVersion.version}"
        }

        val handledHttpObject = executeAllHandlers(httpObject, handlers)

        logger.info {
            "response status line for ${clientSocket.inetAddress.hostAddress}:${clientSocket.port} " +
                    "= ${handledHttpObject.httpVersion.version} ${handledHttpObject.status.code} ${handledHttpObject.status.description}"
        }

        ResponseWriter(writer, handledHttpObject).write()
    }

    private fun executeAllHandlers(httpObject: HttpObject, handlers: Array<AbstractHandler>): HttpObject {
        var tempHttpObject = httpObject

        for (handler in handlers) {
            logger.trace {
                "executing handler on ${clientSocket.inetAddress.hostAddress}: ${clientSocket.port}" +
                        " = ${handler.javaClass.name}"
            }
            tempHttpObject = handler.handle(tempHttpObject)

            if (handler.error) {
                logger.debug {
                    "error on ${clientSocket.inetAddress.hostAddress}: ${clientSocket.port} " +
                        "handler = ${handler.javaClass.name} | httpObject = $tempHttpObject"
                }
                break
            }
        }

        // If the status is still undecided, it passed through all error checkers
        if (tempHttpObject.status == HttpStatus.UNDECIDED)
            tempHttpObject.status = HttpStatus.OK

        return tempHttpObject
    }

}