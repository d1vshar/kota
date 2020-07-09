package io.github.l0llygag.kota

/**
 * Data class to store server-related configuration.
 *
 * @param port Port on which the server runs.
 * @param publicFolder Folder which is used as root for URLs.
 */
data class ServerConfiguration(
    val port: Int,
    val publicFolder: String
)