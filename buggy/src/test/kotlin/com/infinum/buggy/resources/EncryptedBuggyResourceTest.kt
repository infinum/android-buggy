package com.infinum.buggy.resources

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

class EncryptedBuggyResourceTest {

    @Test
    fun `resource should allow to open stream and correctly encrypt delegated resource`() {
        // Give
        val delegateBytes = byteArrayOf(1, 2, 3)
        val delegateResource = BytesBuggyResource("bytes", delegateBytes)
        val aesKey = KeyGenerator.getInstance("AES").apply { init(256) }.generateKey()
        val iv = IvParameterSpec(ByteArray(16) { 0 })
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            init(Cipher.ENCRYPT_MODE, aesKey, iv)
        }
        val encryptedResource = EncryptedBuggyResource(delegateResource, cipher)

        // When
        val inputStream = encryptedResource.openStream()


        // Then
        val decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            init(Cipher.DECRYPT_MODE, aesKey, iv)
        }
        val decryptedContent = decryptionCipher.doFinal(inputStream.readBytes())
        assertArrayEquals(delegateBytes, decryptedContent)
    }
}