package com.infinum.buggy.timber.formatter

import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATETIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.UK)

internal class BuggyLogFormatter : LogFormatter {

    override fun format(priority: Int, tag: String?, message: String, t: Throwable?): String {
        val errorString = if (t != null) {
            val writer = StringWriter()
            t.printStackTrace(PrintWriter(writer))
            System.lineSeparator() + writer.toString()
        } else {
            ""
        }
        return listOfNotNull(
            DATETIME_FORMAT.format(Date()),
            tag,
            message,
            errorString,
        ).joinToString(separator = " ")
    }
}
