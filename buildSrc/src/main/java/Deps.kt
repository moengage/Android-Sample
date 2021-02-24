object Versions {
    const val moengage = "11.0.04"
    val timber = "4.7.1"
    val appCompat = "1.0.2"
    val material = "1.0.0"
    val fcm = "20.0.0"
    val location = "17.0.0"
    const val kotlin = "1.4.20"
    const val glide = "4.9.0"
    const val lifecycleVersion = "2.2.0"
    const val pushTemplates = "2.0.03"
    const val cards = "2.0.02"
    const val pushAmpPlus = "3.0.02"
    const val pushKit = "2.0.02"
    const val hmsPushKit = "4.0.4.301"
    const val geofence = "1.0.02"
    const val annotation = "1.1.0"
}

object Deps {
    // support
    val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val material = "com.google.android.material:material:${Versions.material}"
    const val processLifecycleOwner =
        "androidx.lifecycle:lifecycle-process:${Versions.lifecycleVersion}"

    // firebase
    val fcm = "com.google.firebase:firebase-messaging:${Versions.fcm}"

    // location library
    val locationLib = "com.google.android.gms:play-services-location:${Versions.location}"

    // moengage
    const val moengage = "com.moengage:moe-android-sdk:${Versions.moengage}"
    const val pushTemplates = "com.moengage:rich-notification:${Versions.pushTemplates}"
    const val cards = "com.moengage:cards:${Versions.cards}"
    const val pushAmpPlus = "com.moengage:push-amp-plus:${Versions.pushAmpPlus}"
    const val pushKit = "com.moengage:hms-pushkit:${Versions.pushKit}"
    const val geofence = "com.moengage:geofence:${Versions.geofence}"

    const val hmsPushKit = "com.huawei.hms:push:${Versions.hmsPushKit}"

    // logging library
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //glide
    const val glideCore = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    // kotlin libs
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val annotation = "androidx.annotation:annotation:${Versions.annotation}"

}