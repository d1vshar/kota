import http.ServerChild
import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int) {
    private val serverSocket = ServerSocket(port)

    fun listen() {
        while (true) {
            println("Server listening on ${serverSocket.inetAddress.hostAddress}:${serverSocket.localPort}")
            val clientSocket = serverSocket.accept()

            thread {
                ServerChild(clientSocket).run()
            }
        }
    }
}