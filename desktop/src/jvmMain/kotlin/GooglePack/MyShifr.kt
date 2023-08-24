package ru.ragefalcon.tutatoresFX.mygooglelib

import viewmodel.StateVM
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object MyShifr {
        private val fileSecureKey = File(StateVM.dirGoogle,"keystore.ks").path
//        private val fileToken = "tfft.tf"
        private val AES_TRANSFORMATION = "AES"//CBC/PKCS5Padding"
        private val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        private val keyStorePassword = "your_keyStorePassword".toCharArray()
        private val keyPassword = "your_keyPassword".toCharArray()

        private fun deleteFileSecureKey(){
            val f = File(fileSecureKey)
            if (f.exists()) f.delete()
        }

        fun deleteFileToken(fileToken: String){
            val f = File(StateVM.dirGoogle,"$fileToken.tf")
            if (f.exists()) f.delete()
            deleteFileSecureKey()
        }

        fun saveToken(token: String, fileToken: String){

            /** "JCEKS" формат нужен для возможности сохранения симметричных ключей */
            val keyStore: KeyStore = KeyStore.getInstance("JCEKS") //.getInstance(KeyStore.getDefaultType())
            var secretKey: SecretKey? = null
            val f = File(fileSecureKey)
            if (f.exists() && !f.isDirectory()) {
                // do something
                FileInputStream(f).use { keyStoreData ->
                    keyStore.load(
                        keyStoreData,
                        keyStorePassword //secretKey.toString() //
                    )
                }
                val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(keyPassword)
                secretKey = (keyStore.getEntry("key1", entryPassword) as KeyStore.SecretKeyEntry).secretKey
            } else {
                keyStore.load(null, keyStorePassword)
                val keyGenerator: KeyGenerator = KeyGenerator.getInstance(AES_TRANSFORMATION)

                val secureRandom = SecureRandom()
                val keyBitSize = 128
                keyGenerator.init(keyBitSize, secureRandom)

                secretKey = keyGenerator.generateKey()
                val secretKeyEntry = KeyStore.SecretKeyEntry(secretKey)
                val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(keyPassword)
                keyStore.setEntry(
                    "key1",
                    secretKeyEntry,
                    entryPassword
                )
                FileOutputStream(f).use { keyStoreOutputStream ->
                    keyStore.store(
                        keyStoreOutputStream,
                        keyStorePassword
                    )
                }
            }
////                val key: Key = "asdfsadfsadfsafasfd"
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)//, ivSpec)
            val result = cipher.doFinal(token.toByteArray())

            FileOutputStream(File(StateVM.dirGoogle,"$fileToken.tf").path).use { keyStoreOutputStream ->
                keyStoreOutputStream.write(result)
            }
        }
        fun loadToken(fileToken: String):String?{
            val keyStore: KeyStore = KeyStore.getInstance("JCEKS") //.getInstance(KeyStore.getDefaultType())
            var secretKey: SecretKey? = null
            val f = File(fileSecureKey)
            if (f.exists() && !f.isDirectory()) {
                // do something
                FileInputStream(f).use { keyStoreData ->
                    keyStore.load(
                        keyStoreData,
                        keyStorePassword //secretKey.toString() //
                    )
                }
                val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(keyPassword)
                secretKey = (keyStore.getEntry("key1", entryPassword) as KeyStore.SecretKeyEntry).secretKey
                val f2 = File(StateVM.dirGoogle,"$fileToken.tf")
                var result: ByteArray? = null
                if (f2.exists() && !f2.isDirectory()) {
                    // do something
                    FileInputStream(f2).use { tokenFile ->
                        result = tokenFile.readBytes()
                    }
                    val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(keyPassword)
                    secretKey = (keyStore.getEntry("key1", entryPassword) as KeyStore.SecretKeyEntry).secretKey
                    cipher.init(Cipher.DECRYPT_MODE, secretKey)//, ivSpec)
                    return String(cipher.doFinal(result), Charsets.UTF_8)
                } else {
                    return null
                }
            } else {
                return null
            }
        }
    }
