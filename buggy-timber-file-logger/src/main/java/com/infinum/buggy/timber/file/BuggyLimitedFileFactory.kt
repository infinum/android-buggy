package com.infinum.buggy.timber.file

import android.content.Context
import com.infinum.buggy.rollingFileLogger.FileDefaults.DEFAULT_DIRECTORY_NAME
import com.infinum.buggy.rollingFileLogger.FileDefaults.DEFAULT_LOG_FILE_EXTENSION
import com.infinum.buggy.rollingFileLogger.FileDefaults.DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES
import com.infinum.buggy.rollingFileLogger.FileDefaults.DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES
import java.io.File
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DATE_TIME_FORMATTER = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)

