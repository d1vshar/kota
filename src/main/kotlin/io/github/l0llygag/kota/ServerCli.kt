package io.github.l0llygag.kota

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

fun main(args: Array<String>) {
    ServerCli().main(args)
}

/**
 * This class is responsible for parsing command-line options using the Clikt library.
 * [io.github.l0llygag.kota.Server] is initialized with the options.
 *
 * @property port Port at which the server listens for new connections. Provided using --port flag from CLI.
 */
class ServerCli: CliktCommand() {
    private val port: Int by option().int().default(8080)

    override fun run() {
        Server(port).listen()
    }
}

