buildscript {
    // __LATEST_COMPOSE_RELEASE_VERSION__
    val composeVersion = System.getenv("COMPOSE_TEMPLATE_COMPOSE_VERSION") ?: "1.0.0"//-alpha3"
    val kotlin_version = "1.6.10"
//    val kotlin_version = "1.5.31"
    val nav_version = "2.4.0"
    val sqldelight_version = "1.5.1"    // 1.3.0 сработало для iOs

    repositories {
//        mavenCentral().apply {
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        google().apply {
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://dl.bintray.com/soywiz/soywiz")
//            content {
//                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://www.jetbrains.com/intellij-repository/releases")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://jetbrains.bintray.com/intellij-third-party-dependencies")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://kotlin.bintray.com/kotlinx")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }

        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven("https://dl.bintray.com/soywiz/soywiz")
        maven( "https://www.jetbrains.com/intellij-repository/releases" )
        maven( "https://jetbrains.bintray.com/intellij-third-party-dependencies" )
        maven( "https://kotlin.bintray.com/kotlinx" )
        maven( "https://plugins.gradle.org/m2/" )
        maven( "https://dl.bintray.com/kotlin/kotlin-eap" )

//        maven ("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:$sqldelight_version" )
        classpath("org.jetbrains.compose:compose-gradle-plugin:1.1.1") //$composeVersion
        classpath("com.android.tools.build:gradle:7.0.4")
//        classpath("com.android.tools.build:gradle:7.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version" )
//        classpath("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version" )
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version" )
//        classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0" )
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version" )
//        classpath("com.squareup.sqldelight:sqlite-driver:$sqldelight_version" )

//        classpath("org.openjfx:javafx-plugin:0.0.10")
        // __KOTLIN_COMPOSE_VERSION__
//        classpath(kotlin("gradle-plugin", version = "1.5.21"))
    }
}
//apply(plugin = "org.openjfx.javafxplugin")
allprojects {
    repositories {
//        mavenCentral().apply {
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://dl.bintray.com/soywiz/soywiz")
//            content {
//                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://dl.bintray.com/kotlin/kotlinx.html")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://kotlin.bintray.com/kotlin-js-wrappers")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://kotlin.bintray.com/kotlinx")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
//            content {
////                includeGroup("com.soywiz")
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        maven {
//            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }
//        google().apply {
//            content {
//                excludeGroup("Kotlin/Native")
//            }
//        }
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
