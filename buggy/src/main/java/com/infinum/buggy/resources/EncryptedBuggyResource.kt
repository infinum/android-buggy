package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.InputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream

/**
 * Represents a [BuggyResource] that is backed by an encrypted resource.
 *
 * @property delegate Original resource to be encrypted.
 * @property cipher Cipher to be used for encryption.
 */
class EncryptedBuggyResource(
    private val delegate: BuggyResource,
    private val cipher: Cipher,
) : BuggyResource by delegate {

    override fun openStream(): InputStream = CipherInputStream(delegate.openStream(), cipher)
}
