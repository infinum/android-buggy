@file:Suppress("ImportOrdering")

package com.infinum.buggy.sample.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.buggy.sample.R
import com.infinum.buggy.sample.SampleApplication
import com.infinum.buggy.sample.databinding.FragmentRollingLoggerBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

/**
 * Fragment that used to generate logs and exceptions for testing purposes.
 * Since BuggyRollingFileLogger is used, logs will be written to files, check [SampleApplication]
 */
class RollingLoggerFragment : Fragment() {

    private var _binding: FragmentRollingLoggerBinding? = null
    private val binding get() = _binding!!

    private val viewModel = RollingLoggerViewModel()

    private val adapter = RollingLoggerAdapter()

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
        setupAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.onEach { handleEvent(it) }.launchIn(this)
            }
        }
    }

    private fun setupAdapter() {
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(
                recyclerView.context,
                LinearLayoutManager.VERTICAL,
                false,
            )
        }
    }

    @Suppress("BracesOnWhenStatements")
    private fun handleEvent(events: RollingLoggerEvents) {
        when (events) {
            is RollingLoggerEvents.LogsStarted -> {
                Toast.makeText(
                    requireContext(),
                    "Random logs will be generated in the background",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    @Suppress("KotlinConstantConditions", "MagicNumber")
    private fun setupText() {
        val totalMaxSize = SampleApplication.MAX_TOTAL_FILE_SIZE / (1024 * 1024) // MB
        if (totalMaxSize > 1) {
            binding.tvRollingLogger.text = getString(
                R.string.max_size_of_logs_mb,
                totalMaxSize,
            )
        } else {
            binding.tvRollingLogger.text = getString(
                R.string.max_size_of_logs_kb,
                SampleApplication.MAX_TOTAL_FILE_SIZE / 1024,
            )
        }
    }

    @Suppress("TooGenericExceptionThrown", "MagicNumber")
    private fun setupButtons() {
        binding.apply {
            btnGenerateLogs.setOnClickListener {
                viewModel.onGenerateLogsClicked()
            }

            binding.btnException.setOnClickListener {
                throw RuntimeException("This is a test exception")
            }

            btnOpenLatestLog.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                // to avoid UI state management logic
                val logs = getLogs().maxByOrNull { it.lastModified() }?.readLines()?.reversed()
                adapter.submitList(logs)
            }
        }
    }

    private fun getLogs(): List<File> {
        val internalLogsPath = File(requireContext().filesDir, "buggy-logs")
        return internalLogsPath.listFiles()?.toList() ?: emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
