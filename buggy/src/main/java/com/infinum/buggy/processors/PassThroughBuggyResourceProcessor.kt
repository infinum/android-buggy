package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor

class PassThroughBuggyResourceProcessor : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> = resources
}
