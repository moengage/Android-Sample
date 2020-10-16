plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.huawei.agconnect")

}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.moengage.sample.kotlin"
        minSdkVersion(16)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    // support library
    implementation(Deps.processLifecycleOwner)
    implementation(Deps.appCompat)
    implementation(Deps.material)
    // kotlin stdlib
    implementation(Deps.kotlinStdLib)
    // moengage dependency
    implementation(Deps.moengage)
    // Push templates
    implementation(Deps.pushTemplates)
    // cards
    implementation(Deps.cards)
    // push amp plus
    implementation(Deps.pushAmpPlus)
    // Push Kit
    implementation(Deps.pushKit)
    // Huawei dependency for Push Kit
    implementation(Deps.hmsPushKit)
    // firebase dependency for push notification
    implementation(Deps.fcm)
    // location dependency for geo-fences
    implementation(Deps.locationLib)
    // dependency for using gifs
    implementation(Deps.glideCore)
    kapt(Deps.glideCompiler)

    // logging library used in the sample app, not required by the SDK
    implementation(Deps.timber)
}

