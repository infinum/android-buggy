package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * Represents a [BuggyResource] that is backed by a byte array.
 *
 * @property name Name of the resource.
 * @property bytes Byte array content of the resource.
 */
class BytesBuggyResource(
    override val name: String,
    private val bytes: ByteArray,
) : BuggyResource {

    override fun openStream(): InputStream = ByteArrayInputStream(bytes)
}
