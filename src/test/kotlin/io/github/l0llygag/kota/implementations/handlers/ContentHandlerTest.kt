package io.github.l0llygag.kota.implementations.handlers

import io.github.l0llygag.kota.core.MimeType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.nio.file.Path

class ContentHandlerTest {

    private lateinit var contentHandler: ContentHandler

    @BeforeAll
    fun setUp() {
        contentHandler = ContentHandler()
    }

    @Test
    fun `remove url params from path`() {
        assertEquals("/index.html",
            contentHandler.extractPathString("/index.html?val1,val2=2"))
        assertEquals("/public/../index.html",
            contentHandler.extractPathString("/public/../index.html?val1=Hello%20World,val2=question%3F"))
    }

    @Test
    fun `check illegal path access`() {
        assertFalse(contentHandler.isLegalPath("/../index.html"))
        assertFalse(contentHandler.isLegalPath("/../public/index.html"))
        assertTrue(contentHandler.isLegalPath("/pics/../index.html"))
    }

    @Test
    fun `get path object`() {
        assertEquals(Path.of("public/index.html"),
            contentHandler.normalizePath("index.html", "public"))

        assertEquals(Path.of("public/index.html"),
            contentHandler.normalizePath("/", "public"))
    }

    @Test
    fun `get mime type`() {
        assertEquals(MimeType.HTML, contentHandler.determineMimeType("index.html"))
        assertEquals(MimeType.PNG, contentHandler.determineMimeType("pic.png"))
        assertEquals(MimeType.UNKNOWN, contentHandler.determineMimeType("index"))
    }
}