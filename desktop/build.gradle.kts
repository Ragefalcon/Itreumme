import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
    id("org.jetbrains.compose")
//    id("kotlinx-serialization")
//    id("org.jetbrains.kotlin.jvm")
//    id("application")
//    id("com.squareup.sqldelight")
//    id("idea")

//    id("org.openjfx.javafxplugin") version "0.0.10"
}

kotlin {
    val ktor_version = "1.6.2"//"1.4.0"
    val korimVersion = "2.7.0"
    val korioVersion = "2.7.0"
    val sqldelight_version = "1.5.3"    // 1.3.0 сработало для iOs
    val kotlin_version = "1.5.31"
    val coroutine_version = "1.3.9" //1.3.9-native-mt
    val serializer_version = "0.20.0"//
//    val serializer_version = "1.0-M1-1.4.0-rc"//

    jvm {
        withJava()
    }
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common"))


//    implementation "com.google.oauth-client:google-oauth-client:1.31.1"
//    implementation "io.ktor:ktor-client-json-js:$ktor_version"

                /**
                 * To use this feature, you need to include io.ktor:ktor-client-serialization-jvm artifact on
                 * the JVM and io.ktor:ktor-client-serialization-native on iOS.
                 */

//    implementation "io.ktor:ktor-client-okhttp:$ktor_version"
//    implementation "io.ktor:ktor-client-jetty:$ktor_version"
//    implementation "io.ktor:ktor-client-cio:$ktor_version"

//    implementation "ch.qos.logback:logback-classic:$logback_version"
//    implementation "com.fasterxml.jackson.module:jackson-module-kotlin:2.8.+"
//    implementation 'org.jetbrains.kotlin:kotlin-reflect:1.3.61'
//    implementation "ch.qos.logback:logback-classic:1.2.3" // for logging

//                testImplementation "org.jetbrains.kotlin:kotlin-test-junit"

//                implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//                implementation ("com.squareup.sqldelight:runtime:$sqldelight_version")
                implementation ("io.ktor:ktor-server-netty:$ktor_version")
//                implementation ("io.ktor:ktor-server-core:$ktor_version")

//                implementation ("io.ktor:ktor-client-auth:$ktor_version")
//                implementation ("io.ktor:ktor-auth-jwt:$ktor_version")
//                implementation ("io.ktor:ktor-client-android:$ktor_version")
//                implementation ("io.ktor:ktor-client-logging-jvm:$ktor_version")

                implementation("io.ktor:ktor-websockets:$ktor_version")

                implementation("com.soywiz.korlibs.korim:korim-jvm:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio-jvm:$korioVersion")

                implementation("junit:junit:4.13.2")
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
                //                implementation ("io.ktor:ktor-html-builder:$ktor_version")
//                implementation ("io.ktor:ktor-locations:$ktor_version")
//                implementation ("io.ktor:ktor-auth-ldap:$ktor_version")
//                implementation ("io.ktor:ktor-auth:$ktor_version")


//                implementation ("io.ktor:ktor-client-json:$ktor_version")
//                implementation ("io.ktor:ktor-client-json-jvm:$ktor_version")
//                implementation ("io.ktor:ktor-client-serialization-jvm:$ktor_version")
//                implementation ("io.ktor:ktor-client-apache:$ktor_version")
//                implementation ("io.ktor:ktor-client-jackson:$ktor_version")
//
//                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serializer_version")
//
//                // COROUTINE
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutine_version")
//
//                // SERIALIZATION
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serializer_version")
//                implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
//                implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
//                implementation("io.ktor:ktor-client-serialization-jvm:$ktor_version")
//
//                implementation ("io.ktor:ktor-client-core:$ktor_version")
//                implementation ("io.ktor:ktor-client-websockets:$ktor_version")
//                implementation ("no.tornado:tornadofx:1.7.20")

//                implementation fileTree(org.gradle.internal.impldep.bsh.commands.dir: 'libs', include: ['*.jar'])


                //                implementation("org.openjfx:javafx-web:13.0.1")
//Thanks for using https://jar-download.com

//                implementation("androidx.constraintlayout:constraintlayout:2.1.0")
            }
        }
    }

}

//tasks.named<org.gradle.jvm.tasks.Jar>("releaseSourcesJar") {
//    duplicatesStrategy = DuplicatesStrategy.INCLUDE
//}
tasks.named<org.gradle.language.jvm.tasks.ProcessResources>("jvmProcessResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            /**
             * Suggested runtime modules to include: чтобы получить список этих модулей нужно запустить задачу
             * desktop/compose desktop/suggestRuntimeModules
             * */
            modules("java.compiler", "java.naming", "java.instrument" , "java.sql", "jdk.unsupported")
//            modules("java.instrument", "java.sql", "jdk.unsupported")
            packageName = "Itreumme"
            packageVersion = "1.0.003"
        }
    }
}