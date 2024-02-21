package com.infinum.buggy.rolling

/**
 * Interface for logger that logs messages to a file.
 */
interface Logger {
    fun log(priority: Int, tag: String?, message: String, t: Throwable?)
}
