package com.infinum.buggy.sample.logs

sealed class RollingLoggerEvents {

    data object LogsStarted : RollingLoggerEvents()
}
