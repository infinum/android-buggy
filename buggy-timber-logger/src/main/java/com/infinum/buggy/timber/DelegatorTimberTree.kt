package com.infinum.buggy.timber

import timber.log.Timber

/**
 * Timber tree that can delegate logging to injected logger.
 *
 * Example usage:
 * ```
 * Timber.plant(
 *    DelegatorTimberTree(myLogger::log),
 * )
 * ```
 *
 * @property logger Function that logging will be delegated to when log() is called.
 */
class DelegatorTimberTree(
    private val logger: (priority: Int, tag: String?, message: String, t: Throwable?) -> Unit,
) : Timber.Tree() {

    /**
     * Log method that delegates logging to injected logger.
     * @param priority Log level. See [android.util.Log] for possible values.
     * @param tag Log tag.
     * @param message Log message.
     * @param t Optional exception to log.
     */
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logger(priority, tag, message, t)
    }
}
