package com.infinum.buggy

class Buggy private constructor(
    resources: List<BuggyResource>,
    private val processors: List<BuggyResourceProcessor>,
) {

    private val mutableResources: MutableList<BuggyResource> = resources.toMutableList()
    val resources: Collection<BuggyResource> get() = synchronized(this) { mutableResources.toList() }

    fun <R> export(exporter: Exporter<R>): R {
        val processed = processors.fold(resources) { next, processor ->
            processor.process(next).toList()
        }
        return exporter.export(processed)
    }

    @Synchronized
    fun add(resource: BuggyResource) {
        mutableResources += resource
    }

    @Synchronized
    fun remove(resource: BuggyResource) {
        mutableResources -= resource
    }

    fun newBuilder(): Builder = Builder(this)

    class Builder {

        private val resources = mutableListOf<BuggyResource>()
        private val processors = mutableListOf<BuggyResourceProcessor>()

        constructor(buggy: Buggy) {
            resources += buggy.resources
            processors += buggy.processors
        }

        constructor()

        fun add(resource: BuggyResource) = apply {
            resources += resource
        }

        fun remove(resource: BuggyResource) = apply {
            resources -= resource
        }

        fun add(processor: BuggyResourceProcessor) = apply {
            processors += processor
        }

        fun remove(processor: BuggyResourceProcessor) = apply {
            processors -= processor
        }

        fun build(): Buggy = Buggy(
            resources = resources,
            processors = processors,
        )
    }
}
