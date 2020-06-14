package http

import http.requests.RequestParser
import http.requests.RequestProcessor
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ServerChild(private val clientSocket: Socket): Runnable {

    override fun run() {
        val inputStreamReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val outputStreamWriter = PrintWriter(clientSocket.getOutputStream(), true)

        var tmpLine: String?
        var input = ""

        do {
            tmpLine = inputStreamReader.readLine()

            if (tmpLine.isNullOrEmpty()) {
                respond(input, outputStreamWriter)
                input = ""
            }

            input += "\n" + tmpLine
        } while (true)
    }

    private fun respond(req: String, outputStreamWriter: PrintWriter) {
        val parsedRequest = RequestParser(req).getParsedRequest()
        val requestProcessor = RequestProcessor(parsedRequest)
        val validationResult = requestProcessor.validate()

        if (validationResult)
            outputStreamWriter.println(requestProcessor.responseDocument?.generateResponse())
        else
            outputStreamWriter.println(requestProcessor.errorDocument?.generateResponse())
    }

}