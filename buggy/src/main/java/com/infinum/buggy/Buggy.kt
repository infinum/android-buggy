package com.infinum.buggy

class Buggy private constructor(
    private val _resources: MutableList<BuggyResource>,
    private val processors: List<BuggyResourceProcessor>,
) {

    val resources: Collection<BuggyResource> get() = synchronized(this) { _resources.toList() }

    fun <R> export(exporter: Exporter<R>): R {
        val processed = processors.fold(resources) { next, processor ->
            processor.process(next).toList()
        }
        return exporter.export(processed)
    }

    @Synchronized
    fun add(resource: BuggyResource) {
        _resources += resource
    }

    @Synchronized
    fun remove(resource: BuggyResource) {
        _resources -= resource
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
            _resources = resources,
            processors = processors,
        )
    }
}
