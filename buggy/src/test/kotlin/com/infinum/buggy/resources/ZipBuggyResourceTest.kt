package com.infinum.buggy.resources

import com.infinum.buggy.helpers.readAllEntries
import java.nio.charset.StandardCharsets
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ZipBuggyResourceTest {

    @Test
    fun `openStream should create a zip file with all resources included inside`() {
        val resources = listOf(
            TextBuggyResource(name = "entry_1", text = "Hello"),
            TextBuggyResource(name = "entry_2", text = ", "),
            TextBuggyResource(name = "entry_3", text = "world"),
            TextBuggyResource(name = "entry_4", text = "!")
        )

        val zippedResource = ZipBuggyResource(
            name = "zipped",
            included = resources
        )
        val entries = zippedResource.openStream().use {
            ZipInputStream(it).readAllEntries()
        }
        val sortedEntries = entries.sortedBy { it.first.name }

        assertEquals(resources.map { it.name }, sortedEntries.map { it.first.name })
        assertEquals(resources.map { it.text }, sortedEntries.map { it.second.toString(StandardCharsets.UTF_8) })
    }

    @Test
    fun `openStream should create an empty zip file if no resources are included inside`() {
        val resources = emptyList<TextBuggyResource>()

        val zippedResource = ZipBuggyResource(
            name = "zipped",
            included = resources
        )
        val entries = zippedResource.openStream().use {
            ZipInputStream(it).readAllEntries()
        }
        val sortedEntries = entries.sortedBy { it.first.name }

        assertEquals(resources.map { it.name }, sortedEntries.map { it.first.name })
        assertEquals(resources.map { it.text }, sortedEntries.map { it.second.toString(StandardCharsets.UTF_8) })
    }

    @Test
    fun `openStream should throw an exception if a zip file is created with non-unique resources included inside`() {
        val resources = listOf(
            TextBuggyResource(name = "entry_1", text = "Hello"),
            TextBuggyResource(name = "entry_2", text = ", "),
            TextBuggyResource(name = "entry_3", text = "world"),
            TextBuggyResource(name = "entry_1", text = "!")
        )

        val zippedResource = ZipBuggyResource(
            name = "zipped",
            included = resources
        )

        assertThrows<ZipException> {
            zippedResource.openStream().use {
                ZipInputStream(it).readAllEntries()
            }
        }
    }
}
