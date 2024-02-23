package com.infinum.buggy.sample.report

import java.io.File

sealed class ReportProblemEvent {
    class NavigateToEmailApp(
        val sendTo: String,
        val body: String,
        val attachments: List<File>,
    ) : ReportProblemEvent()
}
