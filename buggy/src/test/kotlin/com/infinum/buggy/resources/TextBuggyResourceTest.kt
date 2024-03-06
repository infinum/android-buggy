package com.infinum.buggy.resources

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextBuggyResourceTest {

    @Test
    fun `resource should encapsulate text with default charset`() {
        // Given
        val resourceText = "This is a test text"
        val resource = TextBuggyResource("testResource", resourceText)

        // When
        val resultBytes = resource.openStream().readAllBytes()
        val resultText = String(resultBytes)

        // Then
        assertEquals(resourceText, resultText)
    }

    @Test
    fun `resource should encapsulate text with custom charset`() {
        // Given
        val resourceText = "This is a test text"
        val resource = TextBuggyResource("testResource", resourceText, Charsets.UTF_16)

        // When
        val resultBytes = resource.openStream().readAllBytes()
        val resultText = String(resultBytes, Charsets.UTF_16)

        // Then
        assertEquals(resourceText, resultText)
    }
}