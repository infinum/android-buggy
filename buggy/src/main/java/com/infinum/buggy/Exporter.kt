package com.infinum.buggy

/**
 * Represents a strategy to export [BuggyResource] to a report,
 * Examples of exporters could be a zip file exporter, a mail exporter, etc.
 */
interface Exporter<R> {

        /**
        * Exports the resources to a report.
        *
        * @param resources the resources to be exported.
        * @return the report.
        */
        fun export(resources: Collection<BuggyResource>): R
}