buildscript {
    val composeVersion = System.getenv("COMPOSE_TEMPLATE_COMPOSE_VERSION") ?: "1.0.0"
    val kotlin_version = "1.6.10"
    val nav_version = "2.4.0"
    val sqldelight_version = "1.5.1"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven("https://dl.bintray.com/soywiz/soywiz")
        maven( "https://www.jetbrains.com/intellij-repository/releases" )
        maven( "https://jetbrains.bintray.com/intellij-third-party-dependencies" )
        maven( "https://kotlin.bintray.com/kotlinx" )
        maven( "https://plugins.gradle.org/m2/" )
        maven( "https://dl.bintray.com/kotlin/kotlin-eap" )
    }

    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:$sqldelight_version" )
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.1.1")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version" )
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version" )
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version" )
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven( "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven( "https://dl.bintray.com/kotlin/kotlin-eap" )
        maven( "https://kotlin.bintray.com/kotlinx" )
        maven( "https://plugins.gradle.org/m2/" )
        maven( "https://dl.bintray.com/soywiz/soywiz" )
        maven( "https://dl.bintray.com/kotlin/kotlinx.html" )
        maven( "https://kotlin.bintray.com/kotlin-js-wrappers" )
    }
}
