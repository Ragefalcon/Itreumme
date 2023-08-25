import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
}

kotlin {
    val ktor_version = "1.6.2"
    val sqldelight_version = "1.5.1"
    val kotlin_version = "1.6.10"
    val coroutine_version = "1.5.2"
    val klockVersion = "2.4.8"
    val korimVersion = "2.7.0"
    val korioVersion = "2.7.0"
    val serializer_version = "0.20.0"

    android()
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.jetbrains.kotlin:kotlin-stdlib-common")

                implementation(compose.preview)

                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")

                implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
                implementation("com.soywiz.korlibs.korim:korim:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio:$korioVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

                implementation("io.ktor:ktor-client-auth:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")

                implementation ("com.squareup.sqldelight:runtime:$sqldelight_version")
                implementation("com.squareup.sqldelight:coroutines-extensions:$sqldelight_version")
            }
        }
        named("androidMain") {
            dependencies {
                api("androidx.appcompat:appcompat:1.3.1")
                api("androidx.core:core-ktx:1.6.0")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

                implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version")

                implementation("io.ktor:ktor-client-android:$ktor_version")
                implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")

                implementation("com.squareup.sqldelight:android-driver:$sqldelight_version")
            }
        }
        named("desktopMain") {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                implementation("com.soywiz.korlibs.korim:korim-jvm:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio-jvm:$korioVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutine_version")

                implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight_version")
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
