package com.infinum.buggy.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateLogs()
        setupButton()
    }

    private fun setupButton() {
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            lifecycleScope.launch {
                val exception = RuntimeException("Test")
                Timber.e(exception)
                throw exception
            }
        }
    }

    private fun generateLogs() {
        lifecycleScope.launch {
            while(true){
                Timber.d("Debug test")
                delay(100)
                Timber.e("Error test")
                delay(200)
            }
        }
    }
}
