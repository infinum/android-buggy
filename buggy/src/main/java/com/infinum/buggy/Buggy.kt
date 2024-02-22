package com.infinum.buggy

/**
 * Buggy is a simple resource exporting library that allows you to add, remove and process resources before the export.
 *
 * Example usage:
 * ```
 * val buggy = Buggy.Builder()
 *  .add(TextResource("Hello"))
 *  .add(ZipBuggyResourceProcessor("compressed.zip"))
 *  .build()
 *
 * val exportedFile = buggy.export(FileExporter())
 * ```
 *
 * @param resources initial resources
 * @param processors processors used to process the resources before the export
 */
class Buggy private constructor(
    resources: List<BuggyResource>,
    private val processors: List<BuggyResourceProcessor>,
) {

    private val mutableResources: MutableList<BuggyResource> = resources.toMutableList()

    /**
     * Returns a copy of all resources.
     */
    val resources: Collection<BuggyResource> get() = synchronized(this) { mutableResources.toList() }

    /**
     * Exports the resources using the given [exporter].
     * Before the export, all the resources are processed using the added processors.
     * The order in which processors are applied is the same as the order in the processors list.
     *
     * @param exporter exporter used to export the resources
     * @return exported resource
     */
    fun <R> export(exporter: Exporter<R>): R {
        val processed = processors.fold(resources) { next, processor ->
            processor.process(next).toList()
        }
        return exporter.export(processed)
    }

    /**
     * Adds a new resource to the list of resources.
     *
     * @param resource resource to add
     */
    @Synchronized
    fun add(resource: BuggyResource) {
        mutableResources += resource
    }

    /**
     * Removes the given resource from the list of resources.
     *
     * @param resource resource to remove
     */
    @Synchronized
    fun remove(resource: BuggyResource) {
        mutableResources -= resource
    }

    /**
     * Builder used to create a new instance of [Buggy] with the same set of resources and processors as this instance.
     *
     * @return new builder
     */
    fun newBuilder(): Builder = Builder(this)

    /**
     * Builder used to create a new instance of [Buggy].
     *
     * Example usage:
     * ```
     * val buggy = Buggy.Builder()
     *  .add(TextResource("Hello"))
     *  .add(ZipBuggyResourceProcessor("compressed.zip"))
     *  .build()
     *
     * val exportedFile = buggy.export(FileExporter())
     * ```
     */
    class Builder {

        private val resources = mutableListOf<BuggyResource>()
        private val processors = mutableListOf<BuggyResourceProcessor>()

        /**
         * Creates a new builder with the same set of resources and processors as the given [buggy].
         *
         * @param buggy buggy to copy
         */
        constructor(buggy: Buggy) {
            resources += buggy.resources
            processors += buggy.processors
        }

        /**
         * Creates a new builder with an empty set of resources and processors.
         */
        constructor()

        /**
         * Adds a new resource to the list of resources.
         *
         * @param resource resource to add
         * @return this builder
         */
        fun add(resource: BuggyResource) = apply {
            resources += resource
        }

        /**
         * Removes the given resource from the list of resources.
         *
         * @param resource resource to remove
         * @return this builder
         */
        fun remove(resource: BuggyResource) = apply {
            resources -= resource
        }

        /**
         * Adds a new processor to the list of processors.
         * The order in which the processors are applied during the export is the same as the order in the processors list.
         *
         * @param processor processor to add
         * @return this builder
         */
        fun add(processor: BuggyResourceProcessor) = apply {
            processors += processor
        }

        /**
         * Removes the given processor from the list of processors.
         *
         * @param processor processor to remove
         * @return this builder
         */
        fun remove(processor: BuggyResourceProcessor) = apply {
            processors -= processor
        }

        /**
         * Creates a new instance of [Buggy] with the set of resources and processors added to this builder.
         *
         * @return new instance of [Buggy]
         */
        fun build(): Buggy = Buggy(
            resources = resources,
            processors = processors,
        )
    }
}
