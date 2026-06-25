import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

android {
    compileSdk = 36
    namespace = "com.moengage.example"

    defaultConfig {
        applicationId = "com.moengage.example"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(platform(libs.moengage.bom))
    implementation(libs.moengage.inapp)
    implementation(libs.moengage.pushKit)
    implementation(libs.moengage.pushAmp)
    implementation(libs.moengage.geofence)
    implementation(libs.moengage.inboxCore)
    // logging library used only for demonstration, not required by the SDK.
    implementation(libs.logcat)

    implementation(moengage.androidXCore)
    implementation("androidx.core:core:1.17.0")
    implementation("androidx.work:work-runtime:2.10.5")
    implementation(moengage.androidXCompact)
    implementation(moengage.androidXLifecycle)
    implementation(moengage.gmsPlayLocation)
    implementation(moengage.firebaseMessaging)
    implementation(moengage.glide)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

// To test push on a device:
//   1. Add app/google-services.json for package com.moengage.example
//   2. Uncomment the line below
// apply(plugin = "com.google.gms.google-services")