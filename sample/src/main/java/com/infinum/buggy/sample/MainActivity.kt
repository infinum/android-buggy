package com.infinum.buggy.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.infinum.buggy.sample.databinding.ActivityMainBinding
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
            // todo move this to a separate screen (RollingLoggerActivity)
            exceptionButton.setOnClickListener {
                lifecycleScope.launch {
                    val exception = RuntimeException("Test")
                    Timber.e(exception)
                    throw exception
                }
            }

            logsButton.setOnClickListener {
                lifecycleScope.launch {
                    while (true) {
                        Timber.d("Debug test")
                        delay(100)
                        Timber.e("Error test")
                        delay(200)
                    }
                }
            }

            reportButton.setOnClickListener {
                val intent = Intent(this@MainActivity, ReportProblemActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
