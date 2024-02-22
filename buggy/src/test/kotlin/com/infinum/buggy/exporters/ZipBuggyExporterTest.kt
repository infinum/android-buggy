package com.infinum.buggy.exporters

import com.infinum.buggy.helpers.readAllEntries
import com.infinum.buggy.resources.TextBuggyResource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.zip.ZipInputStream

class ZipBuggyExporterTest {

    private val exportPath = "temp/report.zip"
    private var exportFile: File = File(exportPath)

    @BeforeEach
    fun setUp() {
        exportFile = File(exportPath).apply {
            parentFile?.mkdirs()
            createNewFile()
        }
    }

    @AfterEach
    fun cleanUp() {
        File(exportPath).delete()
    }

    @Test
    fun `export should create a zip file with all resources included inside`() {
        val resources = listOf(
            TextBuggyResource(name = "entry_1", text = "Hello"),
            TextBuggyResource(name = "entry_2", text = ", "),
            TextBuggyResource(name = "entry_3", text = "world"),
            TextBuggyResource(name = "entry_4", text = "!")
        )

        val exporter = ZipBuggyExporter(
            file = exportFile
        )

        val export = exporter.export(resources)

        val entries = export.inputStream().use {
            ZipInputStream(it).readAllEntries()
        }

        val sortedEntries = entries.sortedBy { it.first.name }

        assertEquals(File(exportPath).exists(), true)
        assertEquals(resources.map { it.name }, sortedEntries.map { it.first.name })
        assertEquals(
            resources.map { it.text },
            sortedEntries.map { it.second.toString(Charsets.UTF_8) })
    }

    @Test
    fun `export should create an empty zip file with no entries if no resources are included inside`() {
        val resources = emptyList<TextBuggyResource>()

        val exporter = ZipBuggyExporter(
            file = exportFile
        )

        val export = exporter.export(resources)

        val entries = export.inputStream().use {
            ZipInputStream(it).readAllEntries()
        }

        assertEquals(true, File(exportPath).exists())
        assertEquals(0, entries.size)
    }
}