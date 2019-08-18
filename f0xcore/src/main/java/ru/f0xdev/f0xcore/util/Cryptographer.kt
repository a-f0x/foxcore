package ru.f0xdev.f0xcore.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Cryptographer {
    fun encryptString(string: String, secretKey: String): String {
        val secret = SecretKeySpec(secretKey.toByteArray(), "AES")
        val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val cipherText = cipher.doFinal(string.toByteArray(charset("UTF-8")))
        return Base64.encodeToString(cipherText, Base64.NO_WRAP)
    }

    fun decryptString(cipherText: String, secretKey: String): String {
        try {
            val secret = SecretKeySpec(secretKey.toByteArray(), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secret)
            val decode = Base64.decode(cipherText, Base64.NO_WRAP)
            val decryptString = String(cipher.doFinal(decode), charset("UTF-8"))
            return decryptString
        } catch (e: Exception) {
            e.printStackTrace()
            return cipherText
        }
    }
}