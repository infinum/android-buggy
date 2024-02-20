package com.infinum.buggy.timber.formatter

interface LogFormatter {

    fun format(priority: Int, tag: String?, message: String, t: Throwable?): String
}
