plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.moengage.sampleapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moengage.sampleapp"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.timber)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.cloudMessaging)
    implementation(libs.playLocation)
    implementation(libs.adIdentifier)
    implementation(libs.glideCore) // required by MoEngage to display InApp
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // MoEngage Dependencies
    implementation(moengage.core)
    implementation(moengage.geofence)
    implementation(moengage.pushKit)
    implementation(moengage.richNotification)
    implementation(moengage.deviceTrigger)
    implementation(moengage.pushAmp)
    implementation(moengage.inapp)
}

apply(plugin = "com.google.gms.google-services")