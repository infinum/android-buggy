package com.infinum.buggy.encryption

import javax.crypto.KeyGenerator

internal fun createSecretKey(
    algorithm: String = "AES",
    keySize: Int = 256,
) = KeyGenerator.getInstance(algorithm).apply { init(keySize) }.generateKey()
