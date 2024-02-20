package com.infinum.buggy.resources

import com.infinum.buggy.BuggyResource
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createTempFile
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

/**
 * Zips multiple [BuggyResource]s into a single [BuggyResource].
 *
 * @property name Name of the zipped resource.
 * @property included Collection of [BuggyResource]s to be zipped.
 */
class ZipBuggyResource(
    override val name: String,
    private val included: Collection<BuggyResource>,
) : BuggyResource {
    override fun openStream(): InputStream {
        val output = createTempFile()
        ZipOutputStream(output.outputStream().buffered()).use { zipper ->
            included.forEach {
                val stream = it.openStream()
                val entry = ZipEntry(it.name)
                zipper.putNextEntry(entry)
                stream.copyTo(zipper)
                stream.close()
            }
        }
        return output.inputStream()
    }
}
