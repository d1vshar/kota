package http.handlers

import http.HttpObject

abstract class AbstractHandler {
    var error = false

    abstract fun handle(httpObject: HttpObject): HttpObject
}