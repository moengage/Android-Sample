import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

/**
 * MoEngage credentials never live in source control. Put them in `local.properties`:
 *
 *   YOUR_MOENGAGE_WORKSPACE_ID=XXXXXXXXXXXXX
 *
 * Without it the app still builds and runs; the SDK is initialised with the placeholder
 * below and simply has nowhere to report to. The data centre is pinned in `MoEngageSDKHelper`.
 */
val localProperties =
    Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) file.inputStream().use { load(it) }
    }

fun localProperty(key: String, fallback: String): String =
    (localProperties.getProperty(key) ?: System.getenv(key) ?: fallback).trim()

android {
    namespace = "com.moengage.sampleapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.moengage.sampleapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "YOUR_MOENGAGE_WORKSPACE_ID",
            "\"${localProperty("YOUR_MOENGAGE_WORKSPACE_ID", "YOUR_YOUR_MOENGAGE_WORKSPACE_ID")}\"",
        )
        buildConfigField(
            "String",
            "MOENGAGE_DATA_CENTER",
            "\"${localProperty("MOENGAGE_DATA_CENTER", "DATA_CENTER_1")}\"",
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Firebase Cloud Messaging — the transport MoEngage push rides on.
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // MoEngage: core (+ pushbase + FCM), in-app, inbox, cards, rich push, geofence, RTT.
    implementation(platform(libs.moengage.android.bom))
    implementation(libs.inapp)
    implementation(libs.inbox.core)
    implementation(libs.cards.core)
    implementation(libs.geofence)
    implementation(libs.realtime.trigger)
    implementation(libs.rich.notification)
    implementation(libs.glide) // required by MoEngage to render in-app images
    // Geofence declares play-services-location as compileOnly, so the app must supply it.
    implementation(libs.play.services.location)

    implementation(libs.timber)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}

/**
 * `google-services.json` is not checked in. Drop yours into `app/` and the plugin wires
 * FCM up automatically; without it the build still succeeds (push just stays inert).
 */
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
} else {
    logger.lifecycle("BrewBar: app/google-services.json missing — FCM push will be inert. See README.md.")
}
