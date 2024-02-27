package com.infinum.buggy.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityMainBinding
import com.infinum.buggy.sample.decrypt.EncryptDecryptActivity
import com.infinum.buggy.sample.logs.RollingLoggerActivity
import com.infinum.buggy.sample.report.ReportProblemActivity

/**
 * Main activity of the sample app.
 * Contains buttons for navigating to other activities/examples.
 * Examples:
 * 1. Report problem - represents standard use case for library - generate report base on log files and send it to mail
 * 2. Encrypt/Decrypt - example of us encrypting and decrypting report (logs in example)
 * 3. Plain text export - example of exporting logs in plain text with Buggy
 * 4. Logs - example for generating logs using Timber and rolling logger (logs are saved in files and are rotated when exceeding size limit)
 */
class MainActivity : AppCompatActivity() {

    @Suppress("LateinitUsage")
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupButtons()
    }

    @Suppress("MagicNumber")
    private fun setupButtons() {
        viewBinding.apply {
            // represents standard use case for library (generate report and send it)
            reportButton.setOnClickListener {
                val intent = Intent(this@MainActivity, ReportProblemActivity::class.java)
                startActivity(intent)
            }

            encryptDecryptButton.setOnClickListener {
                val intent = Intent(this@MainActivity, EncryptDecryptActivity::class.java)
                startActivity(intent)
            }

            plainTextExportButton.setOnClickListener {
                // todo
            }

            logsButton.setOnClickListener {
                val intent = Intent(this@MainActivity, RollingLoggerActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
