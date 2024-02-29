package com.infinum.buggy.resources

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import javax.crypto.Cipher

class EncryptedBuggyResourceTest {

    @Test
    fun `resource should allow to open stream`() {
        // Give
        val delegateBytes = byteArrayOf(1, 2, 3)
        val delegateResource = BytesBuggyResource("bytes", delegateBytes)
        val cipher = Cipher.getInstance("AES")
        val encryptedResource = EncryptedBuggyResource(delegateResource, cipher)

        // When
        val inputStream = encryptedResource.openStream()

        // Then
        assertNotNull(inputStream)
    }
}