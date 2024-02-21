package com.infinum.buggy.timber

import timber.log.Timber

/**
 * Timber tree that can delegate logging to injected logger.
 */
class DelegatorTimberTree(
    private val logger: (priority: Int, tag: String?, message: String, t: Throwable?) -> Unit
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logger(priority, tag, message, t)
    }


}
