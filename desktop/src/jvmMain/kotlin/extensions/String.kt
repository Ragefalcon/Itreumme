package extensions


/**
 * Тут что то пока еще не работает... лушче воспользоваться тем что в файле MyShifr
 * */
/*
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun String.encrypt1(seed : String): String {
    val keyGenerator = KeyGenerator.getInstance("AES")
    val secureRandom = SecureRandom.getInstance("SHA1PRNG")
    secureRandom.setSeed(seed.toByteArray())

    keyGenerator.init(128, secureRandom)
    val skey = keyGenerator.generateKey()
    val rawKey : ByteArray = skey.encoded

    val skeySpec = SecretKeySpec(rawKey, "AES")
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
    val byteArray = cipher.doFinal(this.toByteArray())

    return byteArray.toString()
}

fun String.encrypt(password: String): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until charArray.size){
        iv[i] = charArray[i].toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

    val encryptedValue = cipher.doFinal(this.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedValue)//, Base64.DEFAULT)
}

fun String.decrypt(password: String): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in 0 until charArray.size){
        iv[i] = charArray[i].toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
//    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

    val decryptedByteValue = cipher.doFinal(Base64.getDecoder().decode(this))//, Base64.DEFAULT
    return String(decryptedByteValue)
}*/
