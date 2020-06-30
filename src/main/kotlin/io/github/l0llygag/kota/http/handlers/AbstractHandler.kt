package io.github.l0llygag.kota.http.handlers

import io.github.l0llygag.kota.http.HttpObject

/**
 * Abstract class for a handler. Handler operate on the [io.github.l0llygag.kota.http.HttpObject] parsed according to
 * the request. These are responsible for filling and modifying data for the response.
 *
 * @property error Error state of the handler.
 */
abstract class AbstractHandler {
    var error = false

    /**
     * Function to execute the handler.
     * @return Modified (handled) [io.github.l0llygag.kota.http.HttpObject].
     */
    abstract fun handle(httpObject: HttpObject): HttpObject
}