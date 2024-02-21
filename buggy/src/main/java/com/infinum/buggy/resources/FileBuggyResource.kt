package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.DataInputStream
import java.io.File
import java.io.InputStream

/**
 * Represents a [BuggyResource] that is backed by a [File].
 *
 */
class FileBuggyResource(
    override val name: String,
    private val file: File,
) : BuggyResource {
    override fun openStream(): InputStream = file.inputStream()
}