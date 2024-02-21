package com.infinum.buggy.sample

import android.app.Application
import com.infinum.buggy.fileRollingLogger.BuggyFileRollingLogger
import com.infinum.buggy.fileRollingLogger.BuggyLimitedFileFactory
import com.infinum.buggy.timber.DelegatorTimberTree
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    @Suppress("MagicNumber")
    private fun setupTimber() {
        val maxIndividualFileSize = 15 * 1024L
        val fileFactory = BuggyLimitedFileFactory(
            context = this,
            maxTotalFileSizeBytes = 5 * maxIndividualFileSize,
        )
        val buggyFileRollingLogger = BuggyFileRollingLogger(
            fileFactory = fileFactory::createFile,
            maxIndividualFileSizeBytes = maxIndividualFileSize,
        )

        Timber.plant(
            DelegatorTimberTree(buggyFileRollingLogger::log),
        )
        Timber.plant(Timber.DebugTree())
    }
}
