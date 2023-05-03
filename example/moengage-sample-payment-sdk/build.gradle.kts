plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.moengage.sample.payment.sdk"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        viewBinding = true
    }
}

dependencies {

    implementation(projectLibs.androidCoreKtx)
    implementation(projectLibs.appCompat)
    implementation(projectLibs.material)
    implementation(projectLibs.constraintLayout)

    compileOnly(moengage.core)
    compileOnly(moengage.inapp)

    compileOnly(moengage.pushAmpPlus)
    compileOnly(moengage.pushKit)
    compileOnly(moengage.pushAmp)

    compileOnly(moengage.geofence)

    compileOnly(moengage.inboxCore)
    compileOnly(moengage.cardsUi)
    compileOnly(moengage.cardsCore)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(projectLibs.junit)
    androidTestImplementation(projectLibs.expresso)
}