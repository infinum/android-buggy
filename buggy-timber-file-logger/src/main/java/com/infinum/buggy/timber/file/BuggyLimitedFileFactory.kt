package com.infinum.buggy.timber.file

import android.content.Context
import java.io.File
import java.lang.RuntimeException
import java.util.UUID

class BuggyLimitedFileFactory(
    private val context: Context,
    private val maxNumberOfFiles: Int = 5,
) {
    private val filesDir = context.filesDir
    private val fileNames = mutableListOf<String>()

    @Throws(RuntimeException::class)
    fun createFile(currentIndex: Int): File {
        // current index should start from 1
        if (currentIndex % (maxNumberOfFiles + 1) == 0) {
            removeOldestFile()
        }

        val fileName = generateFileName()
        val path = File(filesDir, fileName)

        path.createNewFile()
        fileNames.add(fileName)

        return path
    }

    private fun generateFileName(): String = UUID.randomUUID().toString()

    private fun removeOldestFile() {
        val oldestFile = fileNames.minByOrNull { File(filesDir, it).lastModified() }
        oldestFile?.let {
            File(filesDir, it).delete()
            fileNames.remove(oldestFile)
        }
    }
}
