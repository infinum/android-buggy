package com.infinum.buggy

class Buggy private constructor(
    @PublishedApi
    internal val resources: MutableList<BuggyResource>,
    private val processors: List<BuggyResourceProcessor>,
) {

    fun <R> export(exporter: Exporter<R>): R {
        val processed = processors.fold(resources.toList()) { next, processor ->
            processor.process(next).toList()
        }
        return exporter.export(processed)
    }

    inline fun <reified T> resourcesOfType(): Collection<T> = resources.filterIsInstance<T>()

    fun add(resource: BuggyResource) {
        resources += resource
    }

    fun remove(resource: BuggyResource) {
        resources -= resource
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
