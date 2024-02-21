package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Represents a [BuggyResource] that is backed by a file. User need to provide a path to the file.
 */
class FileFromPathBuggyResource(
    override val name: String,
    private val path: String,
) : BuggyResource {

    @Throws(IOException::class)
    override fun openStream(): InputStream = File(path).inputStream()
}
