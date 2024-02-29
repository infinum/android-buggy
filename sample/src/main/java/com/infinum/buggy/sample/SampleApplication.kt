package com.infinum.buggy.sample

import android.app.Application
import com.infinum.buggy.android.ApplicationInfoBuggyResource
import com.infinum.buggy.android.DeviceInfoBuggyResource
import com.infinum.buggy.rolling.BuggyFileRollingLogger
import com.infinum.buggy.rolling.BuggyLimitedFileFactory
import com.infinum.buggy.timber.DelegatorTimberTree
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    // Sets up Timber with BuggyFileRollingLogger and DelegatorTimberTree
    // DelegatorTimberTree is used to delegate logs to BuggyFileRollingLogger
    // BuggyFileRollingLogger is used to log to files and rotate them when exceeding size limit
    @Suppress("MagicNumber")
    private fun setupTimber() {
        val appInfo = ApplicationInfoBuggyResource(this)
        val deviceInfo = DeviceInfoBuggyResource()

        val fileFactory = BuggyLimitedFileFactory(
            context = this,
            maxTotalFileSizeBytes = MAX_TOTAL_FILE_SIZE,
        )
        val buggyFileRollingLogger = BuggyFileRollingLogger(
            fileFactory = fileFactory::createFile,
            maxIndividualFileSizeBytes = MAX_INDIVIDUAL_FILE_SIZE,
            onFileOpened = { writer ->
                writer.write(appInfo.openStream().readBytes().toString(Charsets.UTF_8))
                writer.newLine()

                writer.write(deviceInfo.openStream().readBytes().toString(Charsets.UTF_8))
                writer.newLine()
            },
        )

        Timber.plant(
            DelegatorTimberTree(buggyFileRollingLogger::log),
        )
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        const val MAX_INDIVIDUAL_FILE_SIZE = 15 * 1024L // 15KB
        const val MAX_TOTAL_FILE_SIZE = 5 * MAX_INDIVIDUAL_FILE_SIZE // 75KB
    }
}
