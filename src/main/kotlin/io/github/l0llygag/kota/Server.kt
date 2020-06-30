package io.github.l0llygag.kota

import io.github.l0llygag.kota.http.ServerChild
import mu.KotlinLogging
import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int) {

    private val logger = KotlinLogging.logger {  }

    private val serverSocket = ServerSocket(port)

    fun listen() {
        logger.info { "server listening on ${serverSocket.inetAddress.hostAddress}:${serverSocket.localPort}" }
        while (true) {
            val clientSocket = serverSocket.accept()
            logger.trace { "spawning thread for ${clientSocket.inetAddress.hostAddress}:${clientSocket.port}" }

            thread {
                ServerChild(clientSocket).run()
            }
        }
    }
}