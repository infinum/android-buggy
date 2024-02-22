package com.infinum.buggy.helpers

import com.infinum.buggy.BuggyResource
import java.io.InputStream
import java.security.Key
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal fun generateKeyPair(size: Int = 2048) = KeyPairGenerator.getInstance("RSA").apply {
    initialize(size)
}.genKeyPair()

internal fun rsaCipher() = Cipher.getInstance("RSA/ECB/PKCS1Padding")
internal fun rsaEncryptionCipher(key: Key) = rsaCipher().apply {
    init(Cipher.ENCRYPT_MODE, key)
}
internal fun rsaDecryptionCipher(key: Key) = rsaCipher().apply {
    init(Cipher.DECRYPT_MODE, key)
}

internal fun aesCipher() = Cipher.getInstance("AES/CBC/PKCS5Padding")
internal fun aesDecryptionCipher(key: SecretKey, iv: IvParameterSpec) = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
    init(Cipher.DECRYPT_MODE, key, iv)
}

internal fun Cipher.decryptInitializationVectorAndKey(content: InputStream, ivLength: Int = 16): Pair<IvParameterSpec, SecretKey> {
    val decryptedStream = CipherInputStream(content, this)
    val decryptedContent = decryptedStream.readAllBytes()
    val iv = decryptedContent.copyOfRange(0, ivLength)
    val key = decryptedContent.copyOfRange(ivLength, decryptedContent.size)
    return IvParameterSpec(iv) to SecretKeySpec(key, "AES")
}

internal fun BuggyResource.decryptUsing(cipher: Cipher): ByteArray = CipherInputStream(openStream(), cipher).readAllBytes()
internal fun BuggyResource.decryptToTextUsing(cipher: Cipher) = decryptUsing(cipher).toString(Charsets.UTF_8)
