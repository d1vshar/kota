import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket


fun main(args: Array<String>) {
    val server = Server(args[0].toInt())
    server.listen()
}

class Server(port: Int) {
    private val serverSocket = ServerSocket(port)

    fun listen() {
        val socket = serverSocket.accept()
        val inputStreamReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val outputStreamWriter = PrintWriter(socket.getOutputStream(), true)

        while (true) {
            val readLine = inputStreamReader.readLine()
            println(readLine)
            println("SENT")
            outputStreamWriter.println("HTTP/1.1 200 OK\n\n")
            outputStreamWriter.println(serve(readLine))
        }
    }

    private fun serve(req: String): String {
        val split = req.split(" ")
        val fileName = split[1]

        return File("public/$fileName").readText()
    }
}