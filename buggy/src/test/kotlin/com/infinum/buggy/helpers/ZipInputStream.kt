package com.infinum.buggy.helpers

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ZipInputStream.readAllEntries(): List<Pair<ZipEntry, ByteArray>> {
    val entries = mutableListOf<Pair<ZipEntry, ByteArray>>()
    var entry = nextEntry
    while (entry != null) {
        val content = readBytes()
        entries += entry to content
        entry = nextEntry
    }
    return entries
}
