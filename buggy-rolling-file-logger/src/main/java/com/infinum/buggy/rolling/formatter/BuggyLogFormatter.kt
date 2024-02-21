package com.infinum.buggy.rolling.formatter

import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATETIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.UK)

/**
 * Default log formatter for Buggy Timber file logger.
 * It formats log lines in the following format:
 * "Datetime Tag Message [Exception]"
 * example : "2024-02-21 09:10:31.819+0100	Debug test"
 */
internal class BuggyLogFormatter : LogFormatter {

    override fun format(priority: Int, tag: String?, message: String, t: Throwable?): String {
        val logLine = listOfNotNull(
            DATETIME_FORMAT.format(Date()),
            tag,
            message,
        ).joinToString(separator = "\t")

        val exceptionLines = t?.let {
            System.lineSeparator() + StringWriter().apply { t.printStackTrace(PrintWriter(this)) }
                .toString()
        } ?: ""

        return logLine + exceptionLines
    }
}
