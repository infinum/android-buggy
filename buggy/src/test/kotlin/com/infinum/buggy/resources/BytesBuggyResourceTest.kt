package com.infinum.buggy.resources

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BytesBuggyResourceTest {

    @Test
    fun `bytes resource should correctly encapsulate byte array`() {
        // Given
        val bytes = byteArrayOf(1, 2, 3, 4, 5)

        // When
        val resource = BytesBuggyResource("bytes", bytes)

        // Then
        assertEquals("bytes", resource.name)
        assertArrayEquals(bytes, resource.openStream().readAllBytes())
    }
}