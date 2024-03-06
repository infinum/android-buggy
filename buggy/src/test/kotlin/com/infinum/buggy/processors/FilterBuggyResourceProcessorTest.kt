package com.infinum.buggy.processors

import com.infinum.buggy.resources.TextBuggyResource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FilterBuggyResourceProcessorTest {

    @Test
    fun `process should filter resources using specified filter`() {
        // Given
        val processor =
            FilterBuggyResourceProcessor { resources -> resources.filter { it.name == "included" } }
        val resources = listOf(
            TextBuggyResource("included", "test"),
            TextBuggyResource("excluded", "test2")
        )

        // When
        val result = processor.process(resources)

        // Then
        assertEquals(1, result.size)
        assertEquals("included", result.first().name)
    }

    @Test
    fun `process should return all resources if no filter is specified`() {
        // Given
        val processor = FilterBuggyResourceProcessor()
        val resources = listOf(
            TextBuggyResource("included", "test"),
            TextBuggyResource("excluded", "test2")
        )

        // When
        val result = processor.process(resources)

        // Then
        assertEquals(2, result.size)
    }

    @Test
    fun `process should return empty collection if no resources satisfy filter`() {
        // Given
        val processor =
            FilterBuggyResourceProcessor { resources -> resources.filter { it.name.contains(".txt") } }
        val resources = listOf(
            TextBuggyResource("included", "test"),
            TextBuggyResource("excluded", "test2")
        )

        // When
        val result = processor.process(resources)

        // Then
        assertEquals(0, result.size)
    }
}