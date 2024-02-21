package com.infinum.buggy.encryption

import java.security.SecureRandom
import javax.crypto.spec.IvParameterSpec

private val RANDOM = SecureRandom()

internal fun createInitializationVector(
    length: Int = 16,
    random: SecureRandom = RANDOM,
) = IvParameterSpec(ByteArray(length).also { random.nextBytes(it) })
