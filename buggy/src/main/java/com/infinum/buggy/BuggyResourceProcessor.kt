package com.infinum.buggy

/**
 * Processes resources before they are included in the bug report.
 * This could be used to compress resources, encrypt them, etc.
 */
fun interface BuggyResourceProcessor {

    /**
     * Processes the resources before they are included in the bug report.
     * This could be used to compress resources, encrypt them, filter them, etc.
     * The method should return a list of processed resources.
     *
     * @param resources the resources to be processed.
     * @return the processed resources.
     */
    fun process(resources: Collection<BuggyResource>): Collection<BuggyResource>
}
