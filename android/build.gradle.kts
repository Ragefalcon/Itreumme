plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.squareup.sqldelight)
}
kotlin{
    jvmToolchain(17)
}
android {

    namespace = "ru.ragefalcon.tutatores"

    compileSdk = 34
    buildToolsVersion = "34"

    defaultConfig {


        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
//            compose = true
        viewBinding = true
    }
    buildTypes {
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
//        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        named("main") {
            res.srcDirs(
                listOf(
                    "src/main/res/layout/common",
                    "src/main/res/layout/settings",
                    "src/main/res/layout/finance",
                    "src/main/res/layout/time",
                    "src/main/res/layout/journal",
                    "src/main/res/layout/avatar",
                    "src/main/res/layout",
                    "src/main/res"
                )
            )
        }
    }
}

dependencies {
    implementation(projects.common)

    implementation(libs.androidx.activity.compose)

    kapt(libs.google.dagger.compiler)
    implementation(libs.google.dagger)

    debugImplementation(libs.squareup.leakcanary)


    implementation(libs.jetbrains.kotlin.reflect)
    implementation(libs.androidx.gridlayout)
    testImplementation(libs.jetbrains.kotlin.test)
    testImplementation(libs.jetbrains.kotlin.test.junit)

    implementation(libs.jetbrains.kotlin.stdlib.jdk7)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)


    implementation(libs.google.android.material)
    implementation(libs.google.android.gms.play.services.auth)
    implementation(libs.google.android.material)
    implementation(libs.github.bumptech.glide)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.squareup.sqldelight.android.driver)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.viewpager)
    testImplementation(libs.junit.junit)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.espresso.contrib)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.android.support.palette.v7)
}