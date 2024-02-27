package com.infinum.buggy.sample.encryptDecrypt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infinum.buggy.sample.databinding.ActivityEncryptDecryptBinding

class EncryptDecryptActivity : AppCompatActivity() {

    @Suppress("LateinitUsage")
    private lateinit var viewBinding: ActivityEncryptDecryptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityEncryptDecryptBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }
}
