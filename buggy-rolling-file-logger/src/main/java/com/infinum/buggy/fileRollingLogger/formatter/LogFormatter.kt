package com.infinum.buggy.fileRollingLogger.formatter

/**
 * Interface for formatting log messages.
 */
interface LogFormatter {

    /**
     * Formats the log message.
     *
     * @param priority Log level. See [android.util.Log] for possible values.
     * @param tag Log tag.
     * @param message Log message.
     * @param t Optional exception.
     * @return Formatted log message.
     */
    fun format(priority: Int, tag: String?, message: String, t: Throwable?): String
}
