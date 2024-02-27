@file:Suppress("ImportOrdering")

package com.infinum.buggy.sample.encryptDecrypt

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.infinum.buggy.sample.databinding.FragmentEncryptDecryptBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class EncryptDecryptFragment : Fragment() {

    private var _binding: FragmentEncryptDecryptBinding? = null
    private val binding get() = _binding!!

    private val viewModel = EncryptDecryptViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEncryptDecryptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.onEach { handleEvent(it) }.launchIn(this)
            }
        }
    }

    @Suppress("BracesOnWhenStatements")
    private fun handleEvent(event: EncryptDecryptEvent) {
        when (event) {
            is EncryptDecryptEvent.ReportGenerated -> {
                Toast.makeText(requireContext(), "Report generated", Toast.LENGTH_SHORT).show()
            }

            is EncryptDecryptEvent.DecryptReport -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, getUri(requireContext(), event.file))
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }

            is EncryptDecryptEvent.DecryptReportFailed -> {
                Toast.makeText(
                    requireContext(),
                    "Failed to decrypt report, make sure to regenerate encrypted report",
                    Toast.LENGTH_LONG,
                )
                    .show()
            }
        }
    }

    private fun getUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file,
    )

    private fun setupButtons() {
        binding.apply {
            btnGenerateReport.setOnClickListener {
                val logs = getLogs() // assumption here is that log files exist
                viewModel.onGenerateReport(logs, requireContext())
            }

            btnDecryptReport.setOnClickListener {
                // assumption here is that encrypted report exists at this path
                val encryptedReport =
                    File(requireContext().filesDir, "buggy-reports/encrypted-buggy-report.zip")
                viewModel.onDecryptReport(encryptedReport, requireContext())
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
