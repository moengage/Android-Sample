buildscript {
  dependencies {
    classpath("com.android.tools.build:gradle:7.1.1")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
  }
}

plugins {
  id("com.android.application") version "7.1.1" apply false
  id("com.android.library") version "7.1.1" apply false
  id("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}