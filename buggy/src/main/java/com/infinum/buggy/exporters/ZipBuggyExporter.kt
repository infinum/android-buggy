package com.infinum.buggy.exporters

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.Exporter
import java.io.BufferedInputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Exporter that exports the resources to a zip file.
 * @property file the file to be used for exporting report.
 * @property bufferSize the buffer size to be used when exporting the resources.
 */
class ZipBuggyExporter(
    private val file: File,
    private val bufferSize: Int = DEFAULT_BUFFER_SIZE,
) : Exporter<File> {
    override fun export(resources: Collection<BuggyResource>): File {
        ZipOutputStream(
            file.outputStream().buffered(bufferSize = bufferSize),
        ).use { zipOutputStream ->
            resources.forEach { resource ->
                val entry = resource.name
                val inputStream = BufferedInputStream(resource.openStream())
                zipOutputStream.putNextEntry(ZipEntry(entry))
                inputStream.use { entryInputStream ->
                    entryInputStream.copyTo(zipOutputStream, bufferSize = bufferSize)
                }
                zipOutputStream.closeEntry()
            }
        }

        return file
    }
}
