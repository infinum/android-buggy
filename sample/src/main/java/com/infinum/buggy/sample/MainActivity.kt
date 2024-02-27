package com.infinum.buggy.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityMainBinding
import com.infinum.buggy.sample.decrypt.EncryptDecryptActivity
import com.infinum.buggy.sample.logs.RollingLoggerActivity
import com.infinum.buggy.sample.report.ReportProblemActivity

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
            logsButton.setOnClickListener {
                val intent = Intent(this@MainActivity, RollingLoggerActivity::class.java)
                startActivity(intent)
            }

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
        }
    }
}
