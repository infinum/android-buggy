package com.infinum.buggy.sample.report

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class ReportProblemViewModel : ViewModel() {

    private val _events = Channel<ReportProblemEvent>()
    val events = _events.receiveAsFlow()

    fun onExport(description: String?) {

    }
}
