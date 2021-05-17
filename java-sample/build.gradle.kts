plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.huawei.agconnect")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.moengage.sample.java"
        minSdkVersion(17)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
              listOf(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
              )
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    // moengage dependency
    implementation(libs.bundles.moengageBundle)
    // 3rd party library
    implementation(libs.bundles.thirdPartyBundle)
    // google services
    implementation(libs.bundles.googleBundle)
    // Huawei dependency for Push Kit
    implementation(libs.hmsPushKit)
    // Annotation processor for Glide used for gifs
    annotationProcessor(libs.glideCompiler)
}