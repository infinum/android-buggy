package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor

/**
 * A [BuggyResourceProcessor] that does not process the resources at all.
 * This is useful when you want to include all resources, as they are, in the bug report.
 */
class PassThroughBuggyResourceProcessor : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> = resources
}
