package com.infinum.buggy.sample.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RollingLoggerViewModel : ViewModel() {

    private val _events = Channel<RollingLoggerEvents>()
    val events = _events.receiveAsFlow()

    fun onGenerateLogsClicked() = viewModelScope.launch {
        _events.send(RollingLoggerEvents.LogsStarted)
        while (true) {
            Timber.d("Debug test")
            delay(100)
            Timber.e("Error test")
            delay(200)
        }
    }
}
