package com.infinum.buggy.resources

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException
import kotlin.io.path.createTempFile
import kotlin.test.assertEquals

class FileBuggyResourceTest {

    @Test
    fun `resource should allow opening stream`() {
        // Given
        val content = "This is a test file content"
        val file = createTempFile("test", ".txt").toFile().apply {
            writeText(content)
        }
        val resource = FileBuggyResource(file)

        // When
        val stream = resource.openStream()
        val result = stream.readBytes().toString(Charsets.UTF_8)

        // Then
        assertEquals(content, result)
    }

    @Test
    fun `resource should allow opening stream even if created from path`() {
        // Given
        val content = "This is a test file content"
        val file = createTempFile("test", ".txt").toFile().apply {
            writeText(content)
        }
        val resource = FileBuggyResource(file.absolutePath)

        // When
        val stream = resource.openStream()
        val result = stream.readBytes().toString(Charsets.UTF_8)

        // Then
        assertEquals(content, result)
    }

    @Test
    fun `resource should throw exception if opening stream for non-existing file`() {
        // Given
        val nonExistingFilePath = "non-existing-file.txt"
        val resource = FileBuggyResource(nonExistingFilePath)

        // When && Then
        assertThrows<FileNotFoundException> {
            resource.openStream()
        }
    }
}