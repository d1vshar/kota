import http.ServerChild
import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(port: Int) {
    private val serverSocket = ServerSocket(port)

    fun listen() {
        println("Server listening on ${serverSocket.inetAddress.hostAddress}:${serverSocket.localPort}")
        while (true) {
            val clientSocket = serverSocket.accept()

            thread {
                ServerChild(clientSocket).run()
            }
        }
    }
}