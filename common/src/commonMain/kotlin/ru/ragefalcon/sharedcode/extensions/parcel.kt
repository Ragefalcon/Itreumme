package ru.ragefalcon.sharedcode.extensions


/**
 * https://ankushg.com/posts/multiplatform-parcelize/
 * */
expect interface Parcelable

@OptIn(ExperimentalMultiplatform::class)
/** @OptionalExpectation класс не обязательно должен иметь actualаналог на каждой платформе.
 * Если мы используем аннотацию и компилируем для платформы, где мы вообще не реализуем аннотацию,
 * компилятор Kotlin просто притворяется, что аннотации никогда не существовало!
 */
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()
