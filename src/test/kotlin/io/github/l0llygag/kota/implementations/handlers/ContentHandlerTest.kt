package io.github.l0llygag.kota.implementations.handlers

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ContentHandlerTest {
    @Test
    fun `false on illegal file access`() {
        val contentHandler = ContentHandler()
        assertFalse(contentHandler.isLegalPath("/../index.html"))
        assertFalse(contentHandler.isLegalPath("/../public/index.html"))
        assertTrue(contentHandler.isLegalPath("/pics/../index.html"))
    }
}