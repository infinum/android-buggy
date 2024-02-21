package com.infinum.buggy.sample.report

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.infinum.buggy.sample.R
import com.infinum.buggy.sample.databinding.FragmentReportProblemBinding
import java.io.File
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReportProblemFragment : Fragment() {

    private val viewModel = ReportProblemViewModel()

    private lateinit var viewBinding: FragmentReportProblemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_report_problem, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentReportProblemBinding.bind(view)

        view.translationZ = 1f

        with(viewBinding) {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            btnSubmit.setOnClickListener { viewModel.onExport(input.text?.toString()) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(200)
            viewBinding.input.requestFocus()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.onEach { handleEvent(it) }.launchIn(this)
            }
        }
    }

    private fun handleEvent(event: ReportProblemEvent) {
        when (event) {
            is ReportProblemEvent.NavigateToEmailApp -> {
                buildEmailIntent(
                    sendTo = event.sendTo,
                    subject = "Report a problem",
                    body = event.body,
                    attachments = event.attachments.map { buggyShareableUri(requireContext(), it) }
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
        attachments: List<Uri>
    ) = Intent(if (attachments.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND).apply {
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
        context.packageName + ".buggy.provider",
        file
    )
}
