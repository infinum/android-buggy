package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor
import com.infinum.buggy.encryption.createInitializationVector
import com.infinum.buggy.encryption.createSecretKey
import com.infinum.buggy.resources.BytesBuggyResource
import com.infinum.buggy.resources.EncryptedBuggyResource
import javax.crypto.Cipher

/**
 * [BuggyResourceProcessor] that encrypts all resources using a one-time generated key and initialization vector.
 * The key is then encrypted using the provided [keyCipher] and the encrypted key is added to the resources.
 * The [resourceCipher] is used to encrypt the resources.
 * [keyCipher] should already be initialized for encryption.
 *
 * @property keyCipher Cipher to be used for encrypting the key.
 * @property resourceCipher Cipher to be used for encrypting the resources.
 * @property keyName Name of the resource that will contain the encrypted key.
 */
class EncryptionBuggyResourceProcessor(
    private val keyCipher: Cipher,
    private val resourceCipher: Cipher,
    private val keyName: String = ".key.der",
) : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> {
        val key = createSecretKey()
        val iv = createInitializationVector()
        resourceCipher.init(Cipher.ENCRYPT_MODE, key, iv)

        val encryptedResources = resources.map { resource -> EncryptedBuggyResource(resource, resourceCipher) }

        val encodedKey = iv.iv + key.encoded
        val keyResource = BytesBuggyResource(keyName, encodedKey)
        val encryptedKeyResource = EncryptedBuggyResource(keyResource, keyCipher)

        return encryptedResources + encryptedKeyResource
    }
}
