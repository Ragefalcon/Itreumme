import org.jetbrains.compose.compose

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.squareup.sqldelight)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    android()
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(libs.jetbrains.kotlin.stdlib.common)

                implementation(compose.preview)


                implementation(libs.soywiz.korlibs.klock)
                implementation(libs.soywiz.korlibs.korim)
                implementation(libs.soywiz.korlibs.korio)

                implementation(libs.jetbrains.kotlin.stdlib.common)
                implementation(libs.jetbrains.kotlinx.coroutines.core)
                implementation(libs.jetbrains.kotlinx.serialization.json)

                implementation(libs.io.ktor.client.auth)
                implementation(libs.io.ktor.client.logging)
                implementation(libs.io.ktor.client.core)
                implementation(libs.io.ktor.client.cio)
                implementation(libs.io.ktor.client.json)
                implementation(libs.io.ktor.client.serialization)

                implementation(libs.squareup.sqldelight.runtime)
                implementation(libs.squareup.sqldelight.coroutines.extensions)
            }
        }
        named("androidMain") {
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.androidx.core.ktx)
                implementation(libs.jetbrains.kotlin.stdlib.jdk8)

                implementation(libs.jetbrains.kotlin.stdlib)
                implementation(libs.jetbrains.kotlin.stdlib.common)
                implementation(libs.jetbrains.kotlinx.coroutines.core)
                implementation(libs.jetbrains.kotlinx.coroutines.android)

                implementation(libs.io.ktor.client.android)
                implementation(libs.io.ktor.client.json.jvm)
                implementation(libs.io.ktor.client.serialization.jvm)

                implementation(libs.squareup.sqldelight.android.driver)
            }
        }
        named("desktopMain") {
            dependencies {
                implementation(libs.soywiz.korlibs.korim.jvm)
                implementation(libs.soywiz.korlibs.korio.jvm)
                implementation(libs.jetbrains.kotlin.stdlib)
                implementation(libs.jetbrains.kotlinx.coroutines.core)
                implementation(libs.jetbrains.kotlinx.coroutines.javafx)
                implementation(libs.squareup.sqldelight.sqlite.driver)
            }
        }
    }
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        named("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
    }
}

sqldelight {
    database("Database") {
        packageName = "ru.ragefalcon.sharedcode"
        sourceFolders = listOf("sqldelight",
            "sharedcode",
            "avatar",
            "common",
            "Finance",
            "Journal",
            "migration",
            "quest",
            "StartData",
            "Time",
            "Test",
        )
    }
    database("DatabaseQuest") {
        packageName = "ru.ragefalcon.sharedcode.quest"
        sourceFolders = listOf("sqldelightQuest")
    }

    database("DatabaseStyle") {
        packageName = "ru.ragefalcon.sharedcode"
        sourceFolders = listOf("sqldelightStyle")
    }
}
