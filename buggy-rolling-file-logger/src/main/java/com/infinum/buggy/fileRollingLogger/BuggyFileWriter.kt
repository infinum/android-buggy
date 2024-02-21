package com.infinum.buggy.fileRollingLogger

import com.infinum.buggy.fileRollingLogger.formatter.LogFormatter
import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.nio.charset.StandardCharsets

/**
 * Writes logs to a file.
 */
class BuggyFileWriter(
    private val file: File,
    private val formatter: LogFormatter,
    private val onFileOpened: (BufferedWriter) -> Unit = {},
) : Closeable {

    private val outputStream: CountingOutputStream by lazy {
        CountingOutputStream(
            file.apply {
                parentFile?.mkdirs()
                createNewFile()
            }.outputStream().buffered(),
            file.length(),
        )
    }

    private val writer: BufferedWriter by lazy {
        outputStream.bufferedWriter(StandardCharsets.UTF_8).apply {
            onFileOpened(this)
        }
    }

    fun log(priority: Int, tag: String?, message: String, t: Throwable?): Long =
        writeAndFlush(formatter.format(priority, tag, message, t))

    override fun close() {
        writer.safeClose()
    }

    private fun writeAndFlush(line: String): Long {
        runCatching {
            writer.write(line)
            writer.newLine()
            // flush always to log possible crash
            writer.flush()
        }
        return outputStream.count
    }

    private fun Closeable.safeClose() = runCatching { close() }.getOrDefault(Unit)
}