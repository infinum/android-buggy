package com.infinum.buggy.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.infinum.buggy.sample.databinding.ActivityMainBinding
import com.infinum.buggy.sample.logs.RollingLoggerActivity
import com.infinum.buggy.sample.report.ReportProblemActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

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

            reportButton.setOnClickListener {
                val intent = Intent(this@MainActivity, ReportProblemActivity::class.java)
                startActivity(intent)
            }

            // todo add plain export
            // todo add option for exporting encrypted logs and decryption of logs
        }
    }
}
