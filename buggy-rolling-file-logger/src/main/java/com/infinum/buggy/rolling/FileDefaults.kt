package com.infinum.buggy.rolling

/**
 * Default values for file related classes.
 * [DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES] - maximum aggregated capacity of all files - 50MB
 * [DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES] - maximum size of individual file - 10MB
 * [DEFAULT_LOG_FILE_EXTENSION] - default log file extension - .log
 * [DEFAULT_DIRECTORY_NAME] - default directory name - buggy-logs
 */
internal object FileDefaults {
    internal const val DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES = 50L * 1024 * 1024 // 50MB
    internal const val DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    internal const val DEFAULT_LOG_FILE_EXTENSION = ".log"
    internal const val DEFAULT_DIRECTORY_NAME = "buggy-logs"
}
