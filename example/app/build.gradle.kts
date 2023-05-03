plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.moengage.example"
        minSdk = 21
        targetSdk = 33
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
    }
    repositories {
        flatDir {
            dirs("libs")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    implementation(moengage.core)
    implementation(moengage.inapp)

    implementation(moengage.pushAmpPlus)
    implementation(moengage.pushKit)
    implementation(moengage.pushAmp)

    implementation(moengage.geofence)

    implementation(moengage.inboxCore)
    implementation(moengage.cardsUi)
    implementation(moengage.cardsCore)
    // logging library used only for demonstration, not required by the SDK.
    implementation(projectLibs.logcat)
    implementation(projectLibs.fcm)
    implementation(projectLibs.androidCoreKtx)
    implementation(projectLibs.appCompat)
    implementation(projectLibs.material)
    implementation(projectLibs.constraintLayout)
    implementation(projectLibs.lifecycleOwner)
    kapt(projectLibs.glideCompiler)
    implementation(projectLibs.glideCore)
    implementation(project(":moengage-sample-payment-sdk"))

    testImplementation("junit:junit:")
    androidTestImplementation(projectLibs.junit)
    androidTestImplementation(projectLibs.expresso)
}

//apply(plugin = "com.google.gms.google-services")