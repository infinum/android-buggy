package com.infinum.buggy.sample.report

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityReportProblemBinding

class ReportProblemActivity : AppCompatActivity() {

    @Suppress("LateinitUsage")
    private lateinit var viewBinding: ActivityReportProblemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityReportProblemBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}
