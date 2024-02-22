package com.infinum.buggy.sample.report

import android.content.Context
import androidx.lifecycle.ViewModel
import com.infinum.buggy.Buggy
import com.infinum.buggy.BuggyResourceProcessor
import com.infinum.buggy.exporters.ZipBuggyExporter
import com.infinum.buggy.processors.EncryptionBuggyResourceProcessor
import com.infinum.buggy.processors.ZipBuggyResourceProcessor
import com.infinum.buggy.resources.EncryptedBuggyResource
import com.infinum.buggy.resources.FileBuggyResource
import com.infinum.buggy.resources.ZipBuggyResource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.File
import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher

class ReportProblemViewModel : ViewModel() {

    private val _events = Channel<ReportProblemEvent>()
    val events = _events.receiveAsFlow()

    lateinit var buggy: Buggy

    fun initBuggy(logs: List<File>) {
        val builderBuilder = Buggy.Builder()
        // add resources to buggy
        logs.forEach {
            builderBuilder.add(FileBuggyResource(it))
        }
        // add processors to buggy, order of processors is important
        builderBuilder.add(
            ZipBuggyResourceProcessor(
                name = "zippedBuggyResource",
            )
        )

        val keyPair = generateKeyPair()
        builderBuilder.add(
            EncryptionBuggyResourceProcessor(
                keyCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
                    init(
                        Cipher.ENCRYPT_MODE,
                        keyPair.public
                    )
                },
                resourceCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"),
            )
        )
        buggy = builderBuilder.build()
    }

    private fun generateKeyPair(): KeyPair =
        KeyPairGenerator.getInstance("RSA").apply {
            initialize(2048)
        }.genKeyPair()


    // Don't pass context to view model in real app this way, this is just for the sake of the example
    fun onExport(description: String?, context: Context) {
        val dir = File(context.filesDir, "buggy-reports")
        val report = File(dir, "buggy-report.zip").apply {
            parentFile?.mkdirs()
            createNewFile()
        }

        buggy.export(
            ZipBuggyExporter(
                file = report
            )
        )

        // todo send event
    }
}
