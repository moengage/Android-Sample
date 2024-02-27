buildscript {
  dependencies {
    classpath("com.android.tools.build:gradle:8.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
  }
}

plugins {
  id("com.android.application") version "8.2.2" apply false
  id("com.android.library") version "8.2.2" apply false
  id("org.jetbrains.kotlin.android") version "1.7.10" apply false
  id("com.google.gms.google-services") version "4.4.1" apply false
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}