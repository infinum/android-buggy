package com.infinum.buggy.sample.plain.export

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityPlainExportBinding

class PlainExportActivity : AppCompatActivity() {

    @Suppress("LateinitUsage")
    private lateinit var viewBinding: ActivityPlainExportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityPlainExportBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}
