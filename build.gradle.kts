// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "http://developer.huawei.com/repo/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.huawei.agconnect:agcp:1.4.1.300")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "http://developer.huawei.com/repo/")
    }
    repositories {
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}