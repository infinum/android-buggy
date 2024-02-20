package com.infinum.buggy.timber.file

import com.infinum.buggy.timber.file.FileDefaults.DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES
import com.infinum.buggy.timber.formatter.BuggyLogFormatter
import java.io.File
import java.util.concurrent.Executors
import timber.log.Timber

class LimitFileTimberTree(
    private val maxIndividualFileSizeBytes: Long = DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES,
    private val appMetadata: String? = null,
    private val fileFactory: () -> File,
) : Timber.Tree() {

    private var currentWriter = createNewWriter()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        runCatching {
            EXECUTOR.execute {
                if (currentWriter.log(priority, tag, message, t) > maxIndividualFileSizeBytes) {
                    currentWriter.close()
                    currentWriter = createNewWriter()
                }
            }
        }
    }

    private fun createNewFile(): File = fileFactory()

    private fun createNewWriter(): TreeWriter =
        TreeWriter(createNewFile(), BuggyLogFormatter(), appMetadata)

    private companion object {
        private val EXECUTOR = Executors.newSingleThreadExecutor {
            Thread(it).apply {
                isDaemon = true
            }
        }
    }
}
