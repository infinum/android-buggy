package com.infinum.buggy.sample.encryptDecrypt

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.buggy.Buggy
import com.infinum.buggy.exporters.ZipBuggyExporter
import com.infinum.buggy.processors.EncryptionBuggyResourceProcessor
import com.infinum.buggy.processors.ZipBuggyResourceProcessor
import com.infinum.buggy.resources.FileBuggyResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher

class EncryptDecryptViewModel : ViewModel() {

    private val _events = Channel<EncryptDecryptEvent>()
    val events = _events.receiveAsFlow()

    private val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA").apply {
        initialize(2048)
    }.genKeyPair()

    fun onGenerateReport(files: List<File>, context: Context) = viewModelScope.launch {
        val buggy = configureBuggy(files)

        generateReport(buggy, context)

        _events.send(EncryptDecryptEvent.ReportGenerated)
    }

    private fun generateReport(buggy: Buggy, context: Context) = viewModelScope.launch {
        val dir = File(context.filesDir, "buggy-reports")
        val report = File(dir, "buggy-report.zip").apply {
            parentFile?.mkdirs()
            createNewFile()
        }

        buggy.export(
            ZipBuggyExporter(
                file = report,
            ),
        )
    }

    private fun configureBuggy(files: List<File>): Buggy {
        with(Buggy.Builder()) {
            // add resources to buggy
            files.forEach {
                add(FileBuggyResource(it))
            }

            // add processors to buggy, order of processors is important
            add(
                ZipBuggyResourceProcessor(
                    name = "zipped-logs",
                ),
            )

            add(
                EncryptionBuggyResourceProcessor(
                    keyCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
                        init(
                            Cipher.ENCRYPT_MODE,
                            keyPair.public,
                        )
                    },
                    resourceCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"),
                ),
            )
            return build()
        }
    }

    fun onDecryptReport(report: File) {
        TODO("Not yet implemented")
    }
}