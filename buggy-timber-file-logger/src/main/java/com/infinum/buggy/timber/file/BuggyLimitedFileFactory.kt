package com.infinum.buggy.timber.file

import android.content.Context
import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_DIRECTORY_NAME
import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_LOG_FILE_EXTENSION
import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES
import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES
import java.io.File
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATE_TIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)

class BuggyLimitedFileFactory(
    context: Context,
    private val maxTotalFileSizeBytes: Long = DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES,
    private val maxIndividualFileSizeBytes: Long = DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES,
    directoryName: String = DEFAULT_DIRECTORY_NAME,
    val logFileNameFactory: () -> String = ::dateTimeFileName,
) {
    private val filesDir = File(context.filesDir, directoryName)

    private val files: List<File>
        get() =
            filesDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    private fun totalLogSize() = files.sumOf { it.length() }

    @Throws(RuntimeException::class)
    fun createFile(): File {
        if (totalLogSize() >= maxTotalFileSizeBytes) {
            freeUpTheSpace(maxIndividualFileSizeBytes)
        }

        val fileName = logFileNameFactory()
        val path = File(filesDir, fileName)

        path.createNewFile()

        return path
    }

    private fun freeUpTheSpace(requestedSize: Long) {
        files.sortedBy { it.lastModified() }.forEach { file ->
            val currentTotalSize = totalLogSize()
            if (currentTotalSize > (maxTotalFileSizeBytes - requestedSize)) {
                files.find { it == file }?.delete()
            }
        }
    }

    companion object {
        private fun dateTimeFileName(): String =
            DATE_TIME_FORMATTER.format(Date()) + DEFAULT_LOG_FILE_EXTENSION
    }
}
