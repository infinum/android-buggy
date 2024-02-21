package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Represents a [BuggyResource] that is backed by a [File].
 *
 */
class FileBuggyResource(
    override val name: String,
    private val file: File,
) : BuggyResource {

    @Throws(IOException::class)
    override fun openStream(): InputStream = file.inputStream()
}
