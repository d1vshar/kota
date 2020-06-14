import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int


fun main(args: Array<String>) {
    ServerCli().main(args)
}

class ServerCli: CliktCommand() {
    private val port: Int by option().int().default(8080)

    override fun run() {
        Server(port).listen()
    }
}

