plugins {
  id("com.android.application") version "8.13.2" apply false
  id("com.android.library") version "8.13.2" apply false
  id("org.jetbrains.kotlin.android") version "1.9.23" apply false
  id("com.google.gms.google-services") version "4.4.4" apply false
}

tasks.register<Delete>("clean") {
  delete(rootProject.buildDir)
}