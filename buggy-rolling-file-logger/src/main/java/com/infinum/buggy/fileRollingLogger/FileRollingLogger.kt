package com.infinum.buggy.fileRollingLogger

/**
 * Interface for logger that logs messages to a file.
 */
interface FileRollingLogger {
    fun log(priority: Int, tag: String?, message: String, t: Throwable?)
}
