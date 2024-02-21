package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor

/**
 * A [BuggyResourceProcessor] that filters the resources.
 * Use the [includeFilter] to filter the resources that should be included in the bug report.
 * By default, all resources are included.
 * All resources that are not returned by the filter are excluded from the bug report.
 *
 * @property includeFilter a function that returns the resources to include in the bug report
 */
class FilterBuggyResourceProcessor(
    private val includeFilter: (Collection<BuggyResource>) -> Collection<BuggyResource> = { it },
) : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> = includeFilter(resources)
}
