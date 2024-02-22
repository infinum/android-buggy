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
    private val file: File,
    override val name: String = file.name,
) : BuggyResource {

    constructor(
        path: String,
    ) : this(File(path))

    @Throws(IOException::class)
    override fun openStream(): InputStream = file.inputStream()
}
