package http.requests

class RequestParser (req: String) {

    private val lines = req.trim().split("\n")

    private fun getHeaders(): List<Pair<String, String>> {
        val regex = "(.*):\\s*(.*)".toRegex()
        val headers = arrayListOf<Pair<String,String>>()

        for (line in lines) {
            if (line.isEmpty()) break

            val matchResult = regex.matchEntire(line)
            matchResult?.let {
                val groupValues = it.groupValues
                headers.add(groupValues[1] to groupValues[2])
            }
        }

        return headers
    }

    private fun getStatusLine(): ParsedRequestLine {
        val regex = "(\\w*)\\s(.*)\\s(HTTP/1\\.\\d)".toRegex()
        val requestLine = ParsedRequestLine("", "", "")

        if (lines.isNotEmpty()) {
            println(lines)
            val matchResult = regex.matchEntire(lines.first())
            println(matchResult?.groupValues)
            matchResult?.let {
                requestLine.method = it.groupValues[1]
                requestLine.target = it.groupValues[2]
                requestLine.version = it.groupValues[3]
            }
        }

        return requestLine
    }

    fun getParsedRequest(): ParsedRequest {
        return ParsedRequest(getStatusLine(), getHeaders())
    }
}

data class ParsedRequest(var requestLine: ParsedRequestLine, var header: List<Pair<String,String>>)
data class ParsedRequestLine(var method: String, var target: String, var version: String)