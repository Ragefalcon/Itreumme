package ru.ragefalcon.sharedcode.models.data

interface SverOpis<out T> where T : SverOpis<T> {
    val sver: Boolean
    fun sver(newSver: Boolean = sver.not()): T
}
