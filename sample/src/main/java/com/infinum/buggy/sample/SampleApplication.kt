package com.infinum.buggy.sample

import android.app.Application
import com.infinum.buggy.rollingFileLogger.BuggyLimitedFileFactory
import com.infinum.buggy.timber.file.LimitFileTimberTree
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    @Suppress("MagicNumber")
    private fun setupTimber() {
        val maxIndividualFileSize = 15 * 1024L
        val fileFactory = com.infinum.buggy.rollingFileLogger.BuggyLimitedFileFactory(
            context = this,
            maxTotalFileSizeBytes = 5 * maxIndividualFileSize,
        )
        val limitFileTimberTree = LimitFileTimberTree(
            maxIndividualFileSizeBytes = maxIndividualFileSize,
            fileFactory = fileFactory::createFile,
        )
        Timber.plant(
            limitFileTimberTree,
        )
        Timber.plant(Timber.DebugTree())
    }
}
