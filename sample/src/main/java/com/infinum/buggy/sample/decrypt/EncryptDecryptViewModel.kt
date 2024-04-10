@file:Suppress("ImportOrdering")

package com.infinum.buggy.sample.decrypt

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
import timber.log.Timber
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.util.zip.ZipFile
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.path.createTempFile

/**
 * ViewModel for generating encrypted report and decrypting the same report example, see [EncryptDecryptFragment].
 * Buggy is configured to export encrypted logs to zip file and send it to mail.
 * First step is to initialize Buggy with log files as [FileBuggyResource].
 * Then, Buggy is configured with [ZipBuggyResourceProcessor] and [EncryptionBuggyResourceProcessor].
 * [ZipBuggyResourceProcessor] zips all resources (logs) into zip file
 * [EncryptionBuggyResourceProcessor] encrypts all resources using AES secret key, then encrypts secret key (used for AES encryption) using RSA public key
 * When user clicks on button, the report is generated and exported to zip file because of [ZipBuggyExporter].
 *
 * When user clicks on decrypt button, the report is decrypted and shared.
 * Process is as follows:
 * 1. Unzip report, extract encrypted secret key and encrypted resources
 * 2. Decrypt secret key (for AES) using private key from key pair (RSA)
 * 3. Decrypt resources using decrypted secret key (AES)
 * 4. Write decrypted resources to file
 * 5. Share decrypted report
 *
 * All files are written to internal storage. That is why context is needed.
 */
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

    // Don't pass context to view model in real app this way, this is just for the sake of the example
    private fun generateReport(buggy: Buggy, context: Context) = viewModelScope.launch {
        val dir = File(context.filesDir, "buggy-reports")
        val report = File(dir, "encrypted-buggy-report.zip").apply {
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

    // Don't pass context to view model in real app this way, this is just for the sake of the example
    fun onDecryptReport(report: File, context: Context) = viewModelScope.launch {
        try {
            // unzip reports
            // zip should contain encrypted key and encrypted resources (based on process we used for generating report)
            // resources are encrypted using AES and key (for resources) is encrypted using RSA
            val (encryptedKeyFile, encryptedResourcesFiles) = unzipReport(report)

            // decrypt key using private key from key pair
            val (iv, key) = decryptKey(encryptedKeyFile, keyPair.private)

            // create file for decrypted report
            val dir = File(context.filesDir, "buggy-reports")
            val decryptedReport = File(dir, "decrypted-buggy-report.zip").apply {
                parentFile?.mkdirs()
                createNewFile()
            }

            // decrypt resources using decrypted key
            decryptResources(encryptedResourcesFiles, key, iv, decryptedReport)

            _events.send(EncryptDecryptEvent.DecryptReport(decryptedReport))
        } catch (e: Exception) {
            Timber.e(e)
            _events.send(EncryptDecryptEvent.DecryptReportFailed)
        }
    }

    private fun unzipReport(
        report: File,
    ): Pair<File, List<File>> {
        var keyFile: File
        var encryptedResources: List<File>
        ZipFile(report).use { zip ->
            zip.entries().asSequence().first { it.name == ".key.der" }.let { entry ->
                keyFile = createTempFile(prefix = "key-").toFile()
                Files.copy(
                    zip.getInputStream(entry),
                    keyFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                )
            }

            encryptedResources =
                zip.entries().asSequence().filter { it.name != ".key.der" }.associateBy { it.name }
                    .map {
                        Timber.d("Unzipping resource: ${it.key}")

                        val tempFile = createTempFile(prefix = "key-").toFile()
                        Files.copy(
                            zip.getInputStream(it.value),
                            tempFile.toPath(),
                            StandardCopyOption.REPLACE_EXISTING,
                        )
                        tempFile
                    }
        }
        return Pair(keyFile, encryptedResources)
    }

    @Suppress("MagicNumber")
    private fun decryptKey(
        keyFile: File,
        privateKey: PrivateKey,
    ): Pair<IvParameterSpec, SecretKey> {
        val keyBytes = Files.readAllBytes(keyFile.toPath())
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding").apply {
            init(Cipher.DECRYPT_MODE, privateKey)
        }
        val ivLength = 16
        val decryptedContent = cipher.doFinal(keyBytes)
        val iv = decryptedContent.copyOfRange(0, ivLength)
        val key = decryptedContent.copyOfRange(ivLength, decryptedContent.size)
        return IvParameterSpec(iv) to SecretKeySpec(key, "AES")
    }

    private fun decryptResources(
        encryptedResources: List<File>,
        key: SecretKey,
        iv: IvParameterSpec,
        decryptedReport: File,
    ) {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
            init(Cipher.DECRYPT_MODE, key, iv)
        }

        // we know that only one resource is in the list in this example (zip of logs)
        // in real app use for each or similar approach
        val resource = encryptedResources[0]
        val bytes = Files.readAllBytes(resource.toPath())
        val decryptedContent = cipher.doFinal(bytes)
        Files.write(decryptedReport.toPath(), decryptedContent)
    }
}
