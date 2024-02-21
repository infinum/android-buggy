package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.helpers.aesCipher
import com.infinum.buggy.helpers.aesDecryptionCipher
import com.infinum.buggy.helpers.decryptInitializationVectorAndKey
import com.infinum.buggy.helpers.decryptToTextUsing
import com.infinum.buggy.helpers.generateKeyPair
import com.infinum.buggy.helpers.rsaDecryptionCipher
import com.infinum.buggy.helpers.rsaEncryptionCipher
import com.infinum.buggy.resources.TextBuggyResource
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class EncryptionBuggyResourceProcessorTest {

    @Test
    fun `process returns encrypted resources and the key`() {
        val resources = listOf(
            TextBuggyResource("first.txt", "This is a test!"),
            TextBuggyResource("second.txt", "Hello, world!"),
        )

        val keyPair = generateKeyPair()
        val processor = EncryptionBuggyResourceProcessor(
            keyCipher = rsaEncryptionCipher(keyPair.public),
            resourceCipher = aesCipher()
        )

        val output = processor.process(resources)

        val keyResource = output.first { it.name == ".key.der" }
        val encryptedResources = output.filter { it.name != ".key.der" }.associateBy { it.name }

        val (iv, key) = keyResource.openStream().use {
            rsaDecryptionCipher(keyPair.private).decryptInitializationVectorAndKey(it)
        }

        val decryptionCipher = aesDecryptionCipher(key, iv)

        val firstText = encryptedResources.getValue("first.txt").decryptToTextUsing(decryptionCipher)
        val secondText = encryptedResources.getValue("second.txt").decryptToTextUsing(decryptionCipher)

        assertEquals("This is a test!", firstText)
        assertEquals("Hello, world!", secondText)
    }

    @Test
    fun `process returns only the key when there is no resources`() {
        val resources = emptyList<BuggyResource>()

        val keyPair = generateKeyPair()
        val processor = EncryptionBuggyResourceProcessor(
            keyCipher = rsaEncryptionCipher(keyPair.public),
            resourceCipher = aesCipher()
        )

        val output = processor.process(resources)

        val keyResources = output.filter { it.name == ".key.der" }
        val encryptedResources = output.filter { it.name != ".key.der" }

        assertEquals(1, keyResources.size)
        assertEquals(0, encryptedResources.size)
    }
}
