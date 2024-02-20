package com.infinum.buggy.timber.file

import java.io.IOException
import java.io.OutputStream

internal class CountingOutputStream(
    private val delegate: OutputStream,
    initialSize: Long,
) : OutputStream() {
    var count = initialSize
        private set

    @Throws(IOException::class)
    override fun write(b: Int) {
        delegate.write(b)
        count++
    }

    @Throws(IOException::class)
    override fun write(buff: ByteArray) {
        delegate.write(buff)
        count += buff.size
    }

    @Throws(IOException::class)
    override fun write(buff: ByteArray, off: Int, len: Int) {
        delegate.write(buff, off, len)
        count += len
    }

    @Throws(IOException::class)
    override fun flush() {
        delegate.flush()
    }

    @Throws(IOException::class)
    override fun close() {
        delegate.close()
    }
}
