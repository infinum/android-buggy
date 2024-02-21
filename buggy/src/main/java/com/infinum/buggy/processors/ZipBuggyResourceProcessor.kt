package com.infinum.buggy.processors

import com.infinum.buggy.BuggyResource
import com.infinum.buggy.BuggyResourceProcessor
import com.infinum.buggy.resources.ZipBuggyResource

/**
 * A [BuggyResourceProcessor] that zips the resources into a single resource.
 * This is useful when you want to include multiple resources as a single resource in the bug report.
 * Use the [includeFilter] to filter the resources that should be included in the zip.
 * By default, all resources are included.
 * All resources that are not included in the zip are returned as is.
 */
class ZipBuggyResourceProcessor(
    private val name: String,
) : BuggyResourceProcessor {
    override fun process(resources: Collection<BuggyResource>): Collection<BuggyResource> = listOf(ZipBuggyResource(name, resources))
}
