package com.infinum.buggy.sample.logs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityRollingLoggerBinding

class RollingLoggerActivity: AppCompatActivity() {
    @Suppress("LateinitUsage")
    private lateinit var viewBinding: ActivityRollingLoggerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityRollingLoggerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}