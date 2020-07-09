package io.github.l0llygag.kota

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.int
import java.io.File

fun main(args: Array<String>) {
    ServerCli().main(args)
}

/**
 * This class is responsible for parsing command-line options using the Clikt library.
 * [io.github.l0llygag.kota.Server] is initialized with the options.
 *
 * @property port Port at which the server listens for new connections. Provided using --port flag from CLI.
 * @property publicFolder The folder in which public files exist. This acts as root folder for server urls.
 */
class ServerCli: CliktCommand() {
    private val port: Int by option().int().default(8080)
    private val publicFolder: String by option("--public").default("public/").validate {
        require(File(it).exists()) { "Not a valid public folder! (make sure folder path ends with '/')." }
    }

    override fun run() {
        Server(ServerConfiguration(
                port,
                publicFolder
            )).listen()
    }
}

