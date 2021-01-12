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
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation("junit:junit:4.12")
    // support library
    implementation(Deps.processLifecycleOwner)
    implementation(Deps.appCompat)
    implementation(Deps.material)
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
    //geofence
    implementation(Deps.geofence)
    // Huawei dependency for Push Kit
    implementation(Deps.hmsPushKit)
    // firebase dependency for push notification
    implementation(Deps.fcm)
    // location dependency for geo-fences
    implementation(Deps.locationLib)
    // dependency for using gifs
    implementation(Deps.glideCore)
    annotationProcessor(Deps.glideCompiler)

    // logging library used in the sample app, not required by the SDK
    implementation(Deps.timber)
}