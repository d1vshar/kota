package io.github.l0llygag.kota.core

import mu.KotlinLogging
import java.net.ServerSocket
import kotlin.concurrent.thread

/**
 * This class is responsible for listening to new connections and spawning [io.github.l0llygag.kota.core.ServerChild] in
 * new threads.
 *
 * @param serverConfiguration This data class holds configuration related to server.
 * @property serverSocket Socket at which the server listens for new connections.
 * @sample io.github.l0llygag.kota.ServerCli.run
 */
class Server(private val serverConfiguration: ServerConfiguration) {

    private val logger = KotlinLogging.logger {  }

    private val serverSocket = ServerSocket(serverConfiguration.port)

    /**
     * Calling this function actually starts the server.
     */
    fun listen() {
        logger.info { "server listening on ${serverSocket.inetAddress.hostAddress}:${serverSocket.localPort}" }
        while (true) {
            val clientSocket = serverSocket.accept()
            logger.trace { "spawning thread for ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}" }

            thread {
                ServerChild(clientSocket, serverConfiguration).run()
            }
        }
    }
}