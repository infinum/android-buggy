package com.infinum.buggy.sample.decrypt

import java.io.File

sealed class EncryptDecryptEvent {
    data object ReportGenerated : EncryptDecryptEvent()
    data class DecryptReport(val file: File) : EncryptDecryptEvent()
    data object DecryptReportFailed : EncryptDecryptEvent()
}
