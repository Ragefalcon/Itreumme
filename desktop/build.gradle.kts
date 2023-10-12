import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvm {
        withJava()
    }
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(projects.common)

                implementation(libs.io.ktor.server.netty)
                implementation(libs.io.ktor.websockets)

                implementation(libs.soywiz.korlibs.korim.jvm)
                implementation(libs.soywiz.korlibs.korio.jvm)

                implementation(libs.junit.junit)
                implementation(libs.junit.jupiter)
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