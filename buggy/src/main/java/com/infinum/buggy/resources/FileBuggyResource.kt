package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Represents a [BuggyResource] that is backed by a [File].
 *  @property file File to be represented as a resource.
 *  @property name Name of the resource.
 *  @throws IOException if the file cannot be opened.
 *
 */
class FileBuggyResource(
    private val file: File,
    override val name: String = file.name,
) : BuggyResource {

    /**
     * Creates a [FileBuggyResource] from the given [path].
     *
     * @param path Path to the file to be represented as a resource.
     * @throws IOException if the file cannot be opened.
     */
    constructor(
        path: String,
    ) : this(File(path))

    @Throws(IOException::class)
    override fun openStream(): InputStream = file.inputStream()
}
