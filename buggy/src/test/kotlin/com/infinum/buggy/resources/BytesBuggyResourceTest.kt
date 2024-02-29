package com.infinum.buggy.resources

import org.junit.jupiter.api.Test

class BytesBuggyResourceTest {

    @Test
    fun `bytes resource should correctly encapsulate byte array`() {
        // Given
        val bytes = byteArrayOf(1, 2, 3, 4, 5)

        // When
        val resource = BytesBuggyResource("bytes", bytes)

        // Then
        assert(resource.name == "bytes")
        assert(resource.openStream().readAllBytes().contentEquals(bytes))
    }
}