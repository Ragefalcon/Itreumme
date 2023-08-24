
plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("com.squareup.sqldelight")

}

android {
//    compileSdkVersion(30)
    compileSdk = 31
    buildToolsVersion = "33-rc1"

    defaultConfig {
//        minSdkVersion(21)
//        targetSdkVersion(30)
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
//        release {
//            minifyEnabled = false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    sourceSets {
        named("main") {
            res.srcDirs(listOf("src/main/res/layout/common",
                "src/main/res/layout/settings",
                "src/main/res/layout/finance",
                "src/main/res/layout/time",
                "src/main/res/layout/journal",
                "src/main/res/layout/avatar",
                "src/main/res/layout",
                "src/main/res"))
        }
    }

}

dependencies {
    val sqldelight_version = "1.5.1"    // 1.3.0 сработало для iOs
    val kotlin_version = "1.6.10"
//    val kotlin_version = "1.5.31"
    val nav_version = "2.3.5"

    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.3.1")

    /**************/

//    implementation ( fileTree(Pair("libs", listOf("*.jar"))))

    implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation ("androidx.gridlayout:gridlayout:1.0.0")
    testImplementation ("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
//    implementation 'androidx.viewmodel:viewmodel-savedstate:1.0.0'

    implementation ("com.google.android.material:material:1.3.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.0")
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("com.github.bumptech.glide:glide:4.9.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.3.1")
    implementation ("androidx.fragment:fragment-ktx:1.3.3")
    implementation ("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation ("com.squareup.sqldelight:android-driver:$sqldelight_version")
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("androidx.viewpager:viewpager:1.0.0")
    testImplementation ("junit:junit:4.12")

//    //!! Добавляем эту зависимость для возможности проведение тестов с SQLDelight
//    testImplementation "com.squareup.sqldelight:sqlite-driver:1.2.2"

    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation ("androidx.test:core:1.3.0")
    androidTestImplementation ("androidx.test:rules:1.3.0")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation ("androidx.test:runner:1.3.0")
    androidTestImplementation ("com.android.support:palette-v7:31.0.0")
//    androidTestImplementation (group: 'androidx.test.ext', name: 'junit', version: '1.1.2')

}