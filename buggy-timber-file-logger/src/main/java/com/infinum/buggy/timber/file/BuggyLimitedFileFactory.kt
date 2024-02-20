package com.infinum.buggy.timber.file

import android.content.Context
import java.io.File
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATE_TIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)

private const val DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES = 50L * 1024 * 1024 // 50MB
private const val DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
private const val DEFAULT_LOG_FILE_EXTENSION = ".log"
private const val DEFAULT_DIRECTORY_NAME = "buggy-logs"

class BuggyLimitedFileFactory(
    context: Context,
    private val maxTotalFileSize: Long = DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES,
    private val maxIndividualFileSize: Long = DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES,
    directoryName: String = DEFAULT_DIRECTORY_NAME,
    val logFileNameFactory: () -> String = ::dateTimeFileName
) {
    private val filesDir = File(context.filesDir, directoryName)

    private val files: List<File>
        get() =
            filesDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    private fun totalLogSize() = files.sumOf { it.length() }

    @Throws(RuntimeException::class)
    fun createFile(): File {

        if (totalLogSize() >= maxTotalFileSize) {
            freeUpTheSpace(maxIndividualFileSize)
        }


        val fileName = logFileNameFactory()
        val path = File(filesDir, fileName)

        path.createNewFile()

        return path
    }

    private fun freeUpTheSpace(requestedSize: Long) {
        files.sortedBy { it.lastModified() }.forEach { file ->
            val currentTotalSize = totalLogSize()
            if (currentTotalSize > (maxIndividualFileSize - requestedSize)) {
                files.find { it == file }?.delete()
            } else {
                return@forEach
            }
        }
    }

    companion object {
        private fun dateTimeFileName(): String =
            DATE_TIME_FORMATTER.format(Date()) + DEFAULT_LOG_FILE_EXTENSION
    }
}
