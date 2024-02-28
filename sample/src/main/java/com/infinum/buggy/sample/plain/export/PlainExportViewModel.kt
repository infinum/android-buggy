package com.infinum.buggy.sample.plain.export

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.buggy.Buggy
import com.infinum.buggy.exporters.ZipBuggyExporter
import com.infinum.buggy.resources.TextBuggyResource
import java.io.File
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for plain text report example, see [PlainExportFragment].
 * Buggy is configured to export plain text to zip file.
 * First step is to initialize Buggy with plain text as [TextBuggyResource].
 * When user clicks on button, the report is generated and exported to zip file because of [ZipBuggyExporter].
 * All files are written to internal storage. That is why context is needed.
 */
class PlainExportViewModel : ViewModel() {

    private val _events = Channel<PlainExportEvent>()
    val events = _events.receiveAsFlow()

    // Don't pass context to view model in real app this way, this is just for the sake of the example
    fun onExport(text: String?, context: Context) = viewModelScope.launch {
        val buggy = setupBuggy(text)

        val dir = File(context.filesDir, "buggy-reports")
        val report = File(dir, "plain-text-buggy-report.zip").apply {
            parentFile?.mkdirs()
            createNewFile()
        }

        buggy.export(
            ZipBuggyExporter(
                file = report,
            ),
        )

        _events.send(PlainExportEvent.ReportGenerated(report))
    }

    private fun setupBuggy(text: String?): Buggy =
        Buggy.Builder()
            .add(TextBuggyResource(text = text ?: "", name = "plain-export.txt"))
            .build()
}
