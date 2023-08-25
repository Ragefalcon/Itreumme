import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    val ktor_version = "1.6.2"
    val korimVersion = "2.7.0"
    val korioVersion = "2.7.0"
    val sqldelight_version = "1.5.3"
    val kotlin_version = "1.5.31"
    val coroutine_version = "1.3.9"
    val serializer_version = "0.20.0"

    jvm {
        withJava()
    }
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(project(":common"))

                implementation ("io.ktor:ktor-server-netty:$ktor_version")
                implementation("io.ktor:ktor-websockets:$ktor_version")

                implementation("com.soywiz.korlibs.korim:korim-jvm:$korimVersion")
                implementation("com.soywiz.korlibs.korio:korio-jvm:$korioVersion")

                implementation("junit:junit:4.13.2")
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
            }
        }
    }

}

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
            packageName = "Itreumme"
            packageVersion = "1.0.003"
        }
    }
}