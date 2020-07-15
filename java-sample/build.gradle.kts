plugins {
  id("com.android.application")
  id("com.google.gms.google-services")
}

android {
  compileSdkVersion(28)

  defaultConfig {
    applicationId = "com.moengage.sample.java"
    minSdkVersion(16)
    targetSdkVersion(28)
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    named("release"){
      isMinifyEnabled = false
      setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
    }
  }
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
  testImplementation("junit:junit:4.12")
  // support library

  implementation(Deps.appCompat)
  implementation(Deps.material)
  // moengage dependency
  implementation(Deps.moengage)
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