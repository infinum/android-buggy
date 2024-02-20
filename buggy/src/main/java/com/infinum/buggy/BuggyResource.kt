package com.infinum.buggy

import java.io.InputStream

/**
 * Represents a resource to be included in the bug report.
 * Once the resource's stream is read, it cannot be read anymore, but it can be reopened.
 * Close the stream after use.
 */
interface BuggyResource {

    /**
     * Unique name of the resource.
     * This could be used as a name of the resource in the generated bug report.
     */
    val name: String

    /**
     * Opens the resource's stream.
     * Be sure to close the stream after usage.
     * Once the stream is read it cannot be read anymore,
     * but the stream can be reopened by calling this method again.
     *
     * @return the resource's stream.
     */
    fun openStream(): InputStream
}
