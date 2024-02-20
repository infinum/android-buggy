package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor
import com.infinum.buggy.resources.ZipBuggyResource

class ZipBuggyResourceProcessor(
    private val name: String,
    private val includeFilter: BuggyResourceProcessor = PassThroughBuggyResourceProcessor(),
) : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> {
        val include = includeFilter.process(resources).toSet()
        val zipped = ZipBuggyResource(name, include)
        val leftover = resources - include
        return leftover + zipped
    }
}
