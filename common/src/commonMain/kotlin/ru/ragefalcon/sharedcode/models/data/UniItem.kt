package ru.ragefalcon.sharedcode.models.data

import ru.ragefalcon.sharedcode.extensions.Parcelable
import ru.ragefalcon.sharedcode.extensions.Parcelize
import kotlin.reflect.KClass

@Parcelize
class UniItem(val item: Parcelable): Parcelable{
    val type = item::class
    fun <T: Parcelable>getItem(t: KClass<T>):T? = if (t == type) item as T else  null
}