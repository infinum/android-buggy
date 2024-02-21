package com.infinum.buggy.fileRollingLogger

/**
 * Default values for file related classes.
 */
internal object FileDefaults {
    internal const val DEFAULT_MAX_AGGREGATED_FILES_CAPACITY_BYTES = 50L * 1024 * 1024 // 50MB
    internal const val DEFAULT_MAX_INDIVIDUAL_FILE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    internal const val DEFAULT_LOG_FILE_EXTENSION = ".log"
    internal const val DEFAULT_DIRECTORY_NAME = "buggy-logs"
}
