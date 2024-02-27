package com.infinum.buggy.sample.plain.export

import java.io.File

sealed class PlainExportEvent {
    data class ReportGenerated(val file: File) : PlainExportEvent()
}
