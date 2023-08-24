import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("kotlinx-serialization")
    id("kotlin-parcelize")
    id("com.squareup.sqldelight")
//    id("maven-publish")
}

kotlin {
    val ktor_version = "1.6.2"//"1.4.0"
    val sqldelight_version = "1.5.1"    // 1.3.0 сработало для iOs
    val kotlin_version = "1.6.10"
//    val kotlin_version = "1.5.31"
    val coroutine_version = "1.5.2" //1.3.9-native-mt
    val klockVersion = "2.4.8"
    val korimVersion = "2.7.0"
    val korioVersion = "2.7.0"
//    val klockVersion = "1.12.0"
    val serializer_version = "0.20.0"//
//    val serializer_version = "1.0-M1-1.4.0-rc"//


    android()
    jvm("desktop")

    sourceSets {
        named("commonMain") {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.jetbrains.kotlin:kotlin-stdlib-common")

                // Needed only for preview.
                implementation(compose.preview)


                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")

                implementation("com.soywiz.korlibs.klock:klock:$klockVersion") // Common
                implementation("com.soywiz.korlibs.korim:korim:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio:$korioVersion")

                // COROUTINE
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")

                // SERIALIZATION
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

                // KTOR
                implementation("io.ktor:ktor-client-auth:$ktor_version")
                implementation("io.ktor:ktor-client-logging:$ktor_version")
                implementation("io.ktor:ktor-client-core:$ktor_version")
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                implementation("io.ktor:ktor-client-json:$ktor_version")
                implementation("io.ktor:ktor-client-serialization:$ktor_version")

                // SQL Delight
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

//            implementation "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializer_version"

                // COROUTINE
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version")
//
//            // SERIALIZATION
//            implementation "org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializer_version"
//
                // KTOR
                implementation("io.ktor:ktor-client-android:$ktor_version")
                implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
                implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")

                // SQL Delight
                implementation("com.squareup.sqldelight:android-driver:$sqldelight_version")
            }
        }
        named("desktopMain") {
            dependencies {
//                implementation kotlin('stdlib-js')
//                implementation(kotlin("stdlib-jdk8"))
//                implementation("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")

                implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                implementation("com.soywiz.korlibs.korim:korim-jvm:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio-jvm:$korioVersion")

                // COROUTINE
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutine_version")

                // SERIALIZATION
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializer_version")

                // KTOR
//                implementation("io.ktor:ktor-server-netty:$ktor_version")
//                implementation("io.ktor:ktor-server-core:$ktor_version")
//                implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
//                implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")

// https://mvnrepository.com/artifact/org.openjfx/javafx-web
//                implementation("org.openjfx:javafx-web:18-ea+1")
//                implementation("org.openjfx:javafx-web:13.0.1")
//                implementation("org.openjfx:javafx-controls:13.0.1")
//                implementation("org.openjfx:javafx-media:13.0.1")
//                implementation("com.sun.webkit:webview-deps:1.3.2")
                // SQL Delight
//                implementation "com.squareup.sqldelight:android-driver:$sqldelight_version"
//                implementation "com.squareup.sqldelight:sqlite-driver:1.4.2"
                implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight_version")
            }
        }
    }
}

android {
//    compileSdkVersion(30)

    compileSdk = 31
    defaultConfig {
//        minSdkVersion(21)
//        targetSdkVersion(30)
        minSdk = 21
        targetSdk = 31
//        versionCode = 1
//        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
//    buildTypes {
//        release {
//            minifyEnabled false
//        }
//    }
//    lintOptions {
//        abortOnError false
//    }

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


//sqldelight {
//    database("AppDatabase") {
//        packageName = "com.example.db"
//    }
//}
sqldelight {
//    com.squareup.sqlite.migrations.Database {
    database("Database") {
        packageName = "ru.ragefalcon.sharedcode"
        // An array of folders where the plugin will read your '.sq' and '.sqm'
        // files. The folders are relative to the existing source set so if you
        // specify ["db"], the plugin will look into 'src/main/db'.
        // Defaults to ["sqldelight"] (src/main/sqldelight)
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
//            "database"
        )
//        this.
//        schemaOutputDirectory = file("ru.ragefalcon.sharedcode")
//        migrationOutputDirectory = file("ru.ragefalcon.sharedcode")
//        deriveSchemaFromMigrations = true
//        verifyMigrations = true

        // The directory where to store '.db' schema files relative to the root
        // of the project. These files are used to verify that migrations yield
        // a database with the latest schema. Defaults to null so the verification
        // tasks will not be created.
//        schemaOutputDirectory = file("src/main/sqldelight/databases")

        // Optionally specify schema dependencies on other gradle projects
//        dependency project(':OtherProject')
    }
    database("DatabaseQuest") {
//    DatabaseQuest {
        packageName = "ru.ragefalcon.sharedcode.quest"
        // An array of folders where the plugin will read your '.sq' and '.sqm'
        // files. The folders are relative to the existing source set so if you
        // specify ["db"], the plugin will look into 'src/main/db'.
        // Defaults to ["sqldelight"] (src/main/sqldelight)
        sourceFolders = listOf("sqldelightQuest")

        // The directory where to store '.db' schema files relative to the root
        // of the project. These files are used to verify that migrations yield
        // a database with the latest schema. Defaults to null so the verification
        // tasks will not be created.
//        schemaOutputDirectory = file("src/main/sqldelight/databases")

        // Optionally specify schema dependencies on other gradle projects
//        dependency project(':OtherProject')
    }

    database("DatabaseStyle") {
//    DatabaseQuest {
        packageName = "ru.ragefalcon.sharedcode"
        // An array of folders where the plugin will read your '.sq' and '.sqm'
        // files. The folders are relative to the existing source set so if you
        // specify ["db"], the plugin will look into 'src/main/db'.
        // Defaults to ["sqldelight"] (src/main/sqldelight)
        sourceFolders = listOf("sqldelightStyle")

        // The directory where to store '.db' schema files relative to the root
        // of the project. These files are used to verify that migrations yield
        // a database with the latest schema. Defaults to null so the verification
        // tasks will not be created.
//        schemaOutputDirectory = file("src/main/sqldelight/databases")

        // Optionally specify schema dependencies on other gradle projects
//        dependency project(':OtherProject')
    }

    // For native targets, whether sqlite should be automatically linked.
    // Defaults to true.
//    linkSqlite = false
}
