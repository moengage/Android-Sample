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
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    // androidx library
    implementation(libs.bundles.androidxBundle)
    // moengage dependency
    implementation(libs.bundles.moengageBundle)
    // 3rd party library
    implementation(libs.bundles.thirdPartyBundle)
    // google services
    implementation(libs.bundles.googleBundle)
    // kotlin stdlib
    implementation(Deps.kotlinStdLib)
    // Huawei dependency for Push Kit
    implementation(libs.pushKit)
    // Annotation processor for Glide used for gifs
    kapt(libs.glideCompiler)
}

