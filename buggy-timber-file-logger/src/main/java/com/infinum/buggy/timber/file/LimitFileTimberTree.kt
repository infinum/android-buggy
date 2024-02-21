package com.infinum.buggy.timber.file

import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES
import com.infinum.buggy.timber.formatter.BuggyLogFormatter
import java.io.BufferedWriter
import java.io.File
import java.util.concurrent.Executors
import timber.log.Timber

/**
 * Timber tree that limits the size of individual log files.
 *
 * @property maxIndividualFileSizeBytes Maximum size of individual log file in bytes.
 * @property onFileOpened Callback that is called when new log file is opened.
 * @property fileFactory Callback that creates new log file. Argument is the wanted size of the file.
 */
class LimitFileTimberTree(
    private val maxIndividualFileSizeBytes: Long = DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES,
    private val onFileOpened: (BufferedWriter) -> Unit = {},
    private val fileFactory: (Long) -> File,
) : Timber.Tree() {

    private var currentWriter = createNewWriter(maxIndividualFileSizeBytes)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        runCatching {
            EXECUTOR.execute {
                if (currentWriter.log(priority, tag, message, t) > maxIndividualFileSizeBytes) {
                    currentWriter.close()
                    currentWriter = createNewWriter(maxIndividualFileSizeBytes)
                }
            }
        }
    }

    private fun createNewFile(neededSpaceBytes: Long): File = fileFactory(neededSpaceBytes)

    private fun createNewWriter(neededSpaceBytes: Long): TreeWriter =
        TreeWriter(createNewFile(neededSpaceBytes), BuggyLogFormatter(), onFileOpened)

    private companion object {
        private val EXECUTOR = Executors.newSingleThreadExecutor {
            Thread(it).apply {
                isDaemon = true
            }
        }
    }
}
