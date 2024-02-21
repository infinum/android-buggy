package com.infinum.buggy.sample

import android.app.Application
import com.infinum.buggy.timber.file.BuggyLimitedFileFactory
import com.infinum.buggy.timber.file.LimitFileTimberTree
import timber.log.Timber

class SampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    private fun setupTimber() {
        val maxIndividualFIleSize = 15 * 1024L
        val fileFactory = BuggyLimitedFileFactory(
            context = this,
            maxTotalFileSizeBytes = 5 * maxIndividualFIleSize,
            maxIndividualFileSizeBytes = maxIndividualFIleSize,
        )
        val limitFileTimberTree = LimitFileTimberTree(
            maxIndividualFileSizeBytes = maxIndividualFIleSize,
            fileFactory = fileFactory::createFile
        )
        Timber.plant(
            limitFileTimberTree
        )
        Timber.plant(Timber.DebugTree())
    }
}