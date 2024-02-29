package com.infinum.buggy.processors

import com.infinum.buggy.resources.TextBuggyResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PassThroughBuggyResourceProcessorTest {

    @Test
    fun `process should return the same resources`() {
        // Given
        val resources = listOf(
            TextBuggyResource("first.txt", "This is a test!"),
            TextBuggyResource("second.txt", "Hello, world!"),
        )

        val processor = PassThroughBuggyResourceProcessor()

        // When
        val output = processor.process(resources)

        // Then
        assertEquals(resources, output)
    }
}