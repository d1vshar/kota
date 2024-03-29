package io.github.l0llygag.kota.core

import io.github.l0llygag.kota.core.handlers.AbstractHandler
import io.github.l0llygag.kota.core.requests.RequestParser
import io.github.l0llygag.kota.core.response.ResponseWriter
import io.github.l0llygag.kota.implementations.handlers.ContentHandler
import io.github.l0llygag.kota.implementations.handlers.HttpMethodHandler
import io.github.l0llygag.kota.implementations.handlers.HttpVersionHandler
import io.github.l0llygag.kota.implementations.handlers.ResponseDateHandler
import mu.KotlinLogging
import java.io.BufferedOutputStream
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

    // handlers need to be decided programmatically using strategies depending upon request
    private val handlers = arrayOf(
        HttpVersionHandler(),
        HttpMethodHandler(), ContentHandler(),
        ResponseDateHandler()
    )

    /**
     * Runs the runnable. Reads the request and responds using [io.github.l0llygag.kota.core.ServerChild.respond] function.
     * Also closes the connection after 30s timeout.
     */
    override fun run() {
        logger.info { "handling request from ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}" }

        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val writer = BufferedOutputStream(clientSocket.getOutputStream())
        val startTime = Instant.now()

        var tmpLine: String
        var request = ""

        tmpLine = reader.readLine()
        while (tmpLine != "") {
            request += tmpLine+"\n"
            tmpLine = reader.readLine()
        }

        respond(request, writer)
    }

    /**
     * Forms the response to be sent to client. Request is parsed using [io.github.l0llygag.kota.core.requests.RequestParser].
     * Response is written using [io.github.l0llygag.kota.core.response.ResponseWriter].
     *
     * @param req Request from the client.
     * @param writer OutputStream of the socket which is used to write.
     */
    private fun respond(req: String, writer: OutputStream) {
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
     * Executes [io.github.l0llygag.kota.core.handlers.AbstractHandler] on [io.github.l0llygag.kota.core.HttpObject]
     * one by one. If any handler returns error, the returned is used to respond to request.
     *
     * @param httpObject The parsed object received from [io.github.l0llygag.kota.core.requests.RequestParser] which
     * will be passed to first handler.
     * @param handlers Array of handler that need to be executed in order.
     * @return [io.github.l0llygag.kota.core.HttpObject] returned by last executed handler.
     */
    private fun executeAllHandlers(httpObject: HttpObject, handlers: Array<AbstractHandler>): HttpObject {
        var tempHttpObject = httpObject

        for (handler in handlers) {
            logger.trace {
                "HANDLER ${clientSocket.inetAddress.hostAddress} ${clientSocket.port}" +
                        " = ${handler.javaClass.name}"
            }
            val (success, returnedHttpObject) = handler.handle(tempHttpObject, serverConfiguration)
            tempHttpObject = returnedHttpObject

            if (!success) {
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