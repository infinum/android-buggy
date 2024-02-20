package com.infinum.buggy.timber.file

import com.infinum.buggy.timber.formatter.BuggyLogFormatter
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class LimitFileTimberTree(
    private val maxFileSizeBytes: Long,
    private val appMetadata: String? = null,
    private val fileFactory: (Int) -> File
) : Timber.Tree() {

    private val currentIndex = AtomicInteger()
    private var currentWriter = createNewWriter()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        runCatching {
            EXECUTOR.execute {
                if (currentWriter.log(priority, tag, message, t) > maxFileSizeBytes) {
                    currentWriter.close()
                    currentWriter = createNewWriter()
                }
            }
        }
    }

    private fun createNewFile(): File = fileFactory(currentIndex.incrementAndGet())

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