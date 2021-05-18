// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        extra.set("kotlinVersion", "1.4.20")
        google()
        mavenCentral()
        maven(url = "https://developer.huawei.com/repo/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("com.google.gms:google-services:4.3.8")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${rootProject.extra.get("kotlinVersion")}")
        classpath("com.huawei.agconnect:agcp:1.4.1.300")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://developer.huawei.com/repo/")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}