package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Represents a [BuggyResource] that is backed by a text.
 *
 * @param name Name of the resource.
 * @param text Text content of the resource.
 * @param charset Charset of the text content.
 */
class TextBuggyResource(
    override val name: String,
    val text: String,
    private val charset: Charset = StandardCharsets.UTF_8,
) : BuggyResource {

    override fun openStream(): InputStream = text.byteInputStream(charset)

    override fun toString(): String = text
}
