package com.infinum.buggy.sample.plain.export

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
import com.infinum.buggy.sample.databinding.FragmentPlainExportBinding
import java.io.File
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Example for generating plain text report.
 * Report is generated from plain text input to zip file.
 * Report then can be shared.
 */
class PlainExportFragment : Fragment() {

    private var _binding: FragmentPlainExportBinding? = null
    private val binding get() = _binding!!

    private val viewModel = PlainExportViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlainExportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.onEach { handleEvent(it) }.launchIn(this)
            }
        }
    }

    private fun setupUi() {
        binding.apply {
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
    }

    private fun handleEvent(event: PlainExportEvent) {
        when (event) {
            is PlainExportEvent.ReportGenerated -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, getUri(requireContext(), event.file))
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun getUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
        context,
        context.packageName + ".provider",
        file,
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
