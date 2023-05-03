buildscript {
  dependencies {
    classpath("com.android.tools.build:gradle:7.2.2")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    //classpath("com.google.gms:google-services:4.3.15")
  }
}

plugins {
  id("com.android.application") version "7.2.2" apply false
  id("com.android.library") version "7.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}