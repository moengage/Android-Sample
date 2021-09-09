plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.huawei.agconnect")

}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.moengage.sample.kotlin"
        minSdkVersion(17)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    // androidx library
    implementation(libs.bundles.androidxBundle)
    // 3rd party library
    implementation(libs.bundles.thirdPartyBundle)
    // google services
    implementation(libs.bundles.googleBundle)
    // kotlin stdlib
    implementation(libs.kotlin)
    // Huawei dependency for Push Kit
    implementation(libs.hmsPushKit)
    // Annotation processor for Glide used for gifs
    kapt(libs.glideCompiler)
    // moengage dependency
    implementation(moengage.core)
    implementation(moengage.cards)
    implementation(moengage.geofence)
    implementation(moengage.pushKit)
    implementation(moengage.inboxUi)
    implementation(moengage.pushAmpPlus)
    implementation(moengage.richNotification)

}

