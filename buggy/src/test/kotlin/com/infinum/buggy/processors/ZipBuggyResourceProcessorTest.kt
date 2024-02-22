package com.infinum.buggy.processors

import com.infinum.buggy.resources.TextBuggyResource
import com.infinum.buggy.resources.ZipBuggyResource
import com.infinum.buggy.helpers.readAllEntries
import java.util.zip.ZipInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class ZipBuggyResourceProcessorTest {

    @Test
    fun `process should convert all files to a zip by default`() {
        val resources = listOf(
            TextBuggyResource(name = "entry_1", text = "Hello"),
            TextBuggyResource(name = "entry_2", text = ", "),
            TextBuggyResource(name = "entry_3", text = "world"),
            TextBuggyResource(name = "entry_4", text = "!")
        )

        val processor = ZipBuggyResourceProcessor(name = "zipped")
        val result = processor.process(resources)

        assertEquals(1, result.size)
        assertEquals("zipped", result.first().name)
        assertInstanceOf(ZipBuggyResource::class.java, result.first())

        val entries = result.first().openStream().use {
            ZipInputStream(it).readAllEntries()
        }
        assertEquals(4, entries.size)
        assertEquals("Hello, world!", entries.sortedBy { it.first.name }.joinToString("") { it.second.toString(Charsets.UTF_8) })
    }

    @Test
    fun `process should convert only files matched by include filter`() {
        val resources = listOf(
            TextBuggyResource(name = "entry_1", text = "Hello"),
            TextBuggyResource(name = "entry_2", text = ", "),
            TextBuggyResource(name = "entry_3", text = "world"),
            TextBuggyResource(name = "entry_4", text = "!")
        )

        val processor = ApplyBuggyResourceProcessor(
            delegate = ZipBuggyResourceProcessor(name = "zipped"),
            applyTo = { list -> list.filter { !it.name.contains("4") } }
        )

        val result = processor.process(resources).toList().sortedBy { it.name }

        assertEquals(2, result.size)
        assertEquals("entry_4", result[0].name)
        assertInstanceOf(TextBuggyResource::class.java, result[0])
        assertEquals("zipped", result[1].name)
        assertInstanceOf(ZipBuggyResource::class.java, result[1])

        val entries = result[1].openStream().use {
            ZipInputStream(it).readAllEntries()
        }
        assertEquals(3, entries.size)
        assertEquals("Hello, world", entries.sortedBy { it.first.name }.joinToString("") { it.second.toString(Charsets.UTF_8) })
    }
}
