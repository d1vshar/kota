package io.github.l0llygag.kota.implementations

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.int
import io.github.l0llygag.kota.core.Server
import io.github.l0llygag.kota.core.ServerConfiguration
import java.io.File

fun main(args: Array<String>) {
    ServerCli().main(args)
}

/**
 * This class is responsible for parsing command-line options using the Clikt library.
 * [io.github.l0llygag.kota.core.Server] is initialized with the options.
 *
 * @property port Port at which the server listens for new connections. Provided using --port flag from CLI.
 * @property publicFolder The folder in which public files exist. This acts as root folder for server urls.
 */
class ServerCli: CliktCommand() {
    private val port: Int by option("-p", "--port",help = "Port to run the server on")
        .int()
        .default(8080)
    private val publicFolder: String by option("-r", "--root", help = "Root folder for the server")
        .default("public/")
        .validate {
            val f = File(it)
            require(f.exists() && f.isDirectory) {
                "Not a valid public folder! (make sure folder path ends with '/')"
            }
        }

    override fun run() {
        Server(
            ServerConfiguration(
                port,
                publicFolder
            )
        ).listen()
    }
}

