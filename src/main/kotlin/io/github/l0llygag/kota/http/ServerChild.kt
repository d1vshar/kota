package io.github.l0llygag.kota.http

import io.github.l0llygag.kota.ServerConfiguration
import io.github.l0llygag.kota.http.enums.HttpStatus
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


/**
 * Every new connection is handed over to it's own `ServerChild` running in a new thread. Receiving requests, responding
 * and closing the connection is this class's responsibility.
 *
 * @param clientSocket The socket of the client which is connected.
 * @param serverConfiguration This data class holds configuration related to server.
 */
class ServerChild(
    private val clientSocket: Socket,
    private val serverConfiguration: ServerConfiguration
): Runnable {

    private val logger = KotlinLogging.logger {  }

    /**
     * Runs the runnable. Reads the request and responds using [io.github.l0llygag.kota.http.ServerChild.respond] function.
     * Also closes the connection after 30s timeout.
     */
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
                logger.info {
                    "timeout on ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}"
                }
                writer.close()
                clientSocket.close()
                break@outer
            }
        } while (true)

    }

    /**
     * Forms the response to be sent to client. Request is parsed using [io.github.l0llygag.kota.http.requests.RequestParser].
     * Response is written using [io.github.l0llygag.kota.http.response.ResponseWriter].
     *
     * @param req Request from the client.
     * @param writer OutputStream of the socket which is used to write.
     */
    private fun respond(req: String, writer: OutputStream) {
        // handlers need to be decided programmatically using strategies depending upon request
        val handlers = arrayOf(HttpVersionHandler(), HttpMethodHandler(), ContentHandler())

        val httpObject = RequestParser(req).getParsedRequest()

        logger.info {
            "REQUEST ${clientSocket.inetAddress.hostAddress} ${clientSocket.port} " +
                "${httpObject.statusLine.httpMethod} ${httpObject.statusLine.path} ${httpObject.statusLine.httpVersion.version}"
        }

        val handledHttpObject = executeAllHandlers(httpObject, handlers)

        logger.info {
            "RESPONSE ${clientSocket.inetAddress.hostAddress} ${clientSocket.port} " +
                    "${handledHttpObject.httpVersion.version} ${handledHttpObject.status.code} ${handledHttpObject.status.description}"
        }

        ResponseWriter(writer, handledHttpObject).write()
    }

    /**
     * Executes [io.github.l0llygag.kota.http.handlers.AbstractHandler] on [io.github.l0llygag.kota.http.HttpObject]
     * one by one. If any handler returns error, the returned is used to respond to request.
     *
     * @param httpObject The parsed object received from [io.github.l0llygag.kota.http.requests.RequestParser] which
     * will be passed to first handler.
     * @param handlers Array of handler that need to be executed in order.
     * @return [io.github.l0llygag.kota.http.HttpObject] returned by last executed handler.
     */
    private fun executeAllHandlers(httpObject: HttpObject, handlers: Array<AbstractHandler>): HttpObject {
        var tempHttpObject = httpObject

        for (handler in handlers) {
            logger.trace {
                "HANDLER ${clientSocket.inetAddress.hostAddress} ${clientSocket.port}" +
                        " = ${handler.javaClass.name}"
            }
            tempHttpObject = handler.handle(tempHttpObject, serverConfiguration)

            if (handler.error) {
                logger.info {
                    "ERROR ${clientSocket.inetAddress.hostAddress} ${clientSocket.port} " +
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