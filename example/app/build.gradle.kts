plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 35
    namespace = "com.moengage.example"

    defaultConfig {
        applicationId = "com.moengage.example"
        minSdk = 23
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(moengage.core)
    implementation(moengage.inapp)
    implementation(moengage.pushKit)
    implementation(moengage.pushAmp)
    implementation(moengage.geofence)
    implementation(moengage.inboxCore)
    // logging library used only for demonstration, not required by the SDK.
    implementation("com.squareup.logcat:logcat:0.1")

    implementation(moengage.androidXCore)
    implementation(moengage.androidXCompact)
    implementation(moengage.androidXLifecycle)
    implementation(moengage.gmsPlayLocation)
    implementation(moengage.firebaseMessaging)
    implementation(moengage.glide)
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

apply(plugin = "com.google.gms.google-services")