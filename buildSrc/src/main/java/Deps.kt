object Versions{
  val moengage = "10.2.02"
  val timber = "4.7.1"
  val appCompat = "1.0.2"
  val material = "1.0.0"
  val fcm = "20.0.0"
  val location = "17.0.0"
  const val kotlin = "1.3.70"
  const val glide = "4.9.0"
}

object Deps{
  // support
  val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
  val material = "com.google.android.material:material:${Versions.material}"
  // firebase
  val fcm = "com.google.firebase:firebase-messaging:${Versions.fcm}"
  // location library
  val locationLib = "com.google.android.gms:play-services-location:${Versions.location}"
  // moengage
  val moengage = "com.moengage:moe-android-sdk:${Versions.moengage}"
  // logging library
  val timber = "com.jakewharton.timber:timber:${Versions.timber}"
  //glide
  const val glideCore = "com.github.bumptech.glide:glide:${Versions.glide}"
  const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
  // kotlin libs
  val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
}