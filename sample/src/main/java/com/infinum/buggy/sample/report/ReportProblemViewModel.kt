package com.infinum.buggy.sample.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinum.buggy.Buggy
import com.infinum.buggy.exporters.ZipBuggyExporter
import com.infinum.buggy.processors.EncryptionBuggyResourceProcessor
import com.infinum.buggy.processors.ZipBuggyResourceProcessor
import com.infinum.buggy.resources.FileBuggyResource
import java.io.File
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for reporting a problem example, see [ReportProblemFragment].
 * Buggy is configured to export encrypted logs to zip file
 * First step is to initialize Buggy with log files as [FileBuggyResource].
 * Then, Buggy is configured with [ZipBuggyResourceProcessor] and [EncryptionBuggyResourceProcessor].
 * [ZipBuggyResourceProcessor] zips all resources (logs) into zip file
 * [EncryptionBuggyResourceProcessor] encrypts all resources using AES secret key, then encrypts secret key (used for AES encryption) using RSA public key
 * When user clicks on button, the report is generated and exported to zip file because of [ZipBuggyExporter].
 * All files are written to internal storage. That is why context is needed.
 */
@Suppress("MagicNumber", "LateinitUsage")
class ReportProblemViewModel : ViewModel() {

    private val _events = Channel<ReportProblemEvent>()
    val events = _events.receiveAsFlow()

    lateinit var buggy: Buggy

    fun initBuggy(logs: List<File>) {
        with(Buggy.Builder()) {
            // add resources to buggy
            logs.forEach {
                add(FileBuggyResource(it))
            }
            // add processors to buggy, order of processors is important
            add(
                ZipBuggyResourceProcessor(
                    name = "zipped-logs",
                ),
            )

            val keyPair = generateKeyPair()
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
            buggy = build()
        }
    }

    @Suppress("MagicNumber")
    private fun generateKeyPair(): KeyPair =
        KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }.genKeyPair()

    // Don't pass context to view model in real app this way, this is just for the sake of the example
    fun onExport(description: String?, context: Context) {
        viewModelScope.launch {
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

            _events.send(
                ReportProblemEvent.NavigateToEmailApp(
                    sendTo = "test@test.com",
                    body = description ?: "",
                    attachments = listOf(report),
                ),
            )
        }
    }
}
