package io.github.l0llygag.kota.core.handlers

import io.github.l0llygag.kota.core.ServerConfiguration
import io.github.l0llygag.kota.core.HttpObject

/**
 * Abstract class for a handler. Handler operate on the [io.github.l0llygag.kota.core.HttpObject] parsed according to
 * the request. These are responsible for filling and modifying data for the response.
 *
 */
abstract class AbstractHandler {
    /**
     * Function to execute the handler.
     *
     * @param httpObject HttpObject to be modified.
     * @param serverConfiguration Configuration of the server.
     * @return Modified (handled) [io.github.l0llygag.kota.core.HttpObject].
     */
    abstract fun handle(httpObject: HttpObject, serverConfiguration: ServerConfiguration): Pair<Boolean, HttpObject>
}