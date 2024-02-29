package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.resources.TextBuggyResource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ApplyBuggyResourceProcessorTest {

    @Test
    fun `process should apply processor to defined subset of resources`() {
        // Given
        val originalResources = listOf(
            TextBuggyResource("first.txt", "This is a test!"),
            TextBuggyResource("second.txt", "Hello, world!"),
            TextBuggyResource("third.txt", "resource3"),
        )

        val processor = ApplyBuggyResourceProcessor(
            // to uppercase and reverse the content
            delegate = { resources ->
                resources.map {
                    TextBuggyResource(
                        it.name.uppercase(),
                        it.openStream()
                            .use { it.readBytes().reversedArray().decodeToString().uppercase() })
                }
            },
            applyTo = { originalResources.subList(0, 2) }
        )

        // When
        val output = processor.process(originalResources).toList()

        // Then
        // processor returns (notApplied + transformed) in that order
        assertEquals("resource3", output[0].openStream().use { it.readBytes().decodeToString() })
        assertEquals("third.txt", output[0].name)
        assertEquals(3, output.size)
        assertEquals(
            "!TSET A SI SIHT",
            output[1].openStream().use { it.readBytes().decodeToString() })
        assertEquals("FIRST.TXT", output[1].name)
        assertEquals("!DLROW ,OLLEH", output[2].openStream().use { it.readBytes().decodeToString() })
        assertEquals("SECOND.TXT", output[2].name)
    }

    @Test
    fun `process should return same resources if applyTo is not defined`() {
        // Given
        val originalResources = listOf(
            TextBuggyResource("first.txt", "This is a test!"),
            TextBuggyResource("second.txt", "Hello, world!"),
            TextBuggyResource("third.txt", "resource3"),
        )

        val processor = ApplyBuggyResourceProcessor(
            // to uppercase and reverse the content
            delegate = { resources ->
                resources.map {
                    TextBuggyResource(
                        it.name.uppercase(),
                        it.openStream()
                            .use { it.readBytes().reversedArray().decodeToString().uppercase() })
                }
            },
            applyTo = { emptyList() }
        )

        // When
        val output = processor.process(originalResources).toList()

        // Then
        // processor returns (notApplied + transformed) in that order
        assertEquals("This is a test!", output[0].openStream().use { it.readBytes().decodeToString() })
        assertEquals("first.txt", output[0].name)
        assertEquals("Hello, world!", output[1].openStream().use { it.readBytes().decodeToString() })
        assertEquals("second.txt", output[1].name)
        assertEquals("resource3", output[2].openStream().use { it.readBytes().decodeToString() })
        assertEquals("third.txt", output[2].name)
    }

    @Test
    fun `process should apply processor to all resources`() {
        // Given
        val originalResources = listOf(
            TextBuggyResource("first.txt", "This is a test!"),
            TextBuggyResource("second.txt", "Hello, world!"),
            TextBuggyResource("third.txt", "resource3"),
        )

        val processor = ApplyBuggyResourceProcessor(
            // to uppercase and reverse the content
            delegate = { resources ->
                resources.map {
                    TextBuggyResource(
                        it.name.uppercase(),
                        it.openStream()
                            .use { it.readBytes().reversedArray().decodeToString().uppercase() })
                }
            },
            applyTo = { originalResources }
        )

        // When
        val output = processor.process(originalResources).toList()

        // Then
        // processor returns (notApplied + transformed) in that order
        assertEquals("!TSET A SI SIHT", output[0].openStream().use { it.readBytes().decodeToString() })
        assertEquals("FIRST.TXT", output[0].name)
        assertEquals("!DLROW ,OLLEH", output[1].openStream().use { it.readBytes().decodeToString() })
        assertEquals("SECOND.TXT", output[1].name)
        assertEquals("3ECRUOSER", output[2].openStream().use { it.readBytes().decodeToString() })
        assertEquals("THIRD.TXT", output[2].name)
    }

    @Test
    fun `process should return empty collection if given empty input`() {
        // Given
        val originalResources = emptyList<BuggyResource>()

        val processor = ApplyBuggyResourceProcessor(
            // to uppercase and reverse the content
            delegate = { resources ->
                resources.map {
                    TextBuggyResource(
                        it.name.uppercase(),
                        it.openStream()
                            .use { it.readBytes().reversedArray().decodeToString().uppercase() })
                }
            },
            applyTo = { originalResources }
        )

        // When
        val output = processor.process(originalResources)

        // Then
        assertEquals(0, output.size)
    }
}