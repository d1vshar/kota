package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.http.HttpObject

abstract class AbstractHandler {
    var error = false

    abstract fun handle(httpObject: HttpObject): HttpObject
}