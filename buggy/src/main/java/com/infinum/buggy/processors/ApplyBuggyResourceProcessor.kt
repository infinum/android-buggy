package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor

/**
 * A [BuggyResourceProcessor] that applies a processor to a subset of resources.
 * This is useful when you want to apply a processor to only some of the resources, and leave the rest as is.
 * Use the [applyTo] to specify which resources should be processed.
 *
 * @property delegate the processor to apply to the subset of resources
 * @property applyTo a function that returns the subset of resources to apply the processor to
 */
class ApplyBuggyResourceProcessor(
    private val delegate: BuggyResourceProcessor,
    private val applyTo: (Collection<BuggyResource>) -> Collection<BuggyResource>,
) : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> {
        val applied = applyTo(resources).toSet()
        val notApplied = resources - applied
        val transformed = delegate.process(applied)
        return notApplied + transformed
    }
}
