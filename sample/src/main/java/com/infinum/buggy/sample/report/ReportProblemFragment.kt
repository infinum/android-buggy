@file:Suppress("ImportOrdering")

package com.infinum.buggy.sample.report

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infinum.buggy.sample.databinding.FragmentReportProblemBinding
import timber.log.Timber
import java.io.File
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Suppress("MagicNumber")
class ReportProblemFragment : Fragment() {

    private val viewModel = ReportProblemViewModel()

    private var _binding: FragmentReportProblemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReportProblemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logs = getLogs() // assumption here is that logs exist
        Timber.d("Logs: $logs")
        viewModel.initBuggy(logs)

        view.translationZ = 1f

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSubmit.setOnClickListener {
                viewModel.onExport(
                    input.text?.toString(),
                    requireContext(),
                )
            }
            input.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                    Unit

                override fun afterTextChanged(s: Editable?) {
                    btnSubmit.isEnabled = s?.isNotEmpty() == true
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(200)
            binding.input.requestFocus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.onEach { handleEvent(it) }.launchIn(this)
            }
        }
    }

    private fun getLogs(): List<File> {
        val internalLogsPath = File(requireContext().filesDir, "buggy-logs")
        return internalLogsPath.listFiles()?.toList() ?: emptyList()
    }

    private fun handleEvent(event: ReportProblemEvent) {
        when (event) {
            is ReportProblemEvent.NavigateToEmailApp -> {
                buildEmailIntent(
                    sendTo = event.sendTo,
                    subject = "Report a problem",
                    body = event.body,
                    attachments = event.attachments.map { buggyShareableUri(requireContext(), it) },
                ).takeIf { it.resolveActivity(requireContext().packageManager) != null }?.let {
                    startActivity(it)
                }
                findNavController().navigateUp()
            }
        }
    }

    private fun buildEmailIntent(
        sendTo: String?,
        subject: String?,
        body: String?,
        attachments: List<Uri>,
    ) =
        Intent(if (attachments.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND).apply {
            type = "text/plain"
            if (attachments.isNotEmpty()) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (attachments.size > 1) {
                    putExtra(Intent.EXTRA_STREAM, ArrayList(attachments.toList()))
                } else {
                    putExtra(Intent.EXTRA_STREAM, attachments.first())
                }
            }
            if (sendTo != null) {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(sendTo))
            }
            if (subject != null) {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            if (body != null) {
                putExtra(Intent.EXTRA_TEXT, body)
            }
        }

    private fun buggyShareableUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file,
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
