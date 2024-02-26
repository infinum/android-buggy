package com.infinum.buggy.sample.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.infinum.buggy.sample.R
import com.infinum.buggy.sample.SampleApplication
import com.infinum.buggy.sample.databinding.FragmentRollingLoggerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RollingLoggerFragment : Fragment() {

    private var _binding: FragmentRollingLoggerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRollingLoggerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupText()
        setupButtons()
    }

    @Suppress("KotlinConstantConditions")
    private fun setupText() {
        val totalMaxSize = SampleApplication.MAX_TOTAL_FILE_SIZE / (1024 * 1024) // MB
        if (totalMaxSize > 1) {
            binding.tvRollingLogger.text = getString(
                R.string.max_size_of_logs_mb,
                totalMaxSize
            )
        } else {
            binding.tvRollingLogger.text = getString(
                R.string.max_size_of_logs_kb,
                SampleApplication.MAX_TOTAL_FILE_SIZE / 1024
            )
        }
    }

    private fun setupButtons() {
        binding.apply {
            btnGenerateLogs.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Random logs will be generated in the background",
                    Toast.LENGTH_SHORT
                ).show()
                lifecycleScope.launch {
                    while (true) {
                        Timber.d("Debug test")
                        delay(100)
                        Timber.e("Error test")
                        delay(200)
                    }
                }
            }

            binding.btnException.setOnClickListener {
                throw RuntimeException("This is a test exception")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}