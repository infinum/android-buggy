package com.infinum.buggy.sample.encryptDecrypt

sealed class EncryptDecryptEvent {
    data object ReportGenerated : EncryptDecryptEvent()
    data object DecryptReport : EncryptDecryptEvent()
}