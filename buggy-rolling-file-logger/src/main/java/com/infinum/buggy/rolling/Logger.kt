package com.infinum.buggy.rolling

/**
 * Interface for logger that logs messages
 */
interface Logger {

    /**
     * Logs a message
     *
     * @param priority Log level. See [android.util.Log] for possible values.
     * @param tag Log tag.
     * @param message Log message.
     * @param t Optional exception to log.
     */
    fun log(priority: Int, tag: String?, message: String, t: Throwable?)
}
