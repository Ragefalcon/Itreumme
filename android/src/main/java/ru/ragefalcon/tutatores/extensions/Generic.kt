package ru.ragefalcon.tutatores.extensions

class Generic<T : Any>(val klass: Class<T>) {
    companion object {
        inline operator fun <reified T : Any> invoke() = Generic(T::class.java)
    }

    fun checkType(t: Any) {
        when {
            klass.isAssignableFrom(t.javaClass) -> println("Correct type")
            else -> println("Wrong type")
        }

    }
}