object Versions {
    const val moengage = "10.4.02"
    val timber = "4.7.1"
    val appCompat = "1.0.2"
    val material = "1.0.0"
    val fcm = "20.0.0"
    val location = "17.0.0"
    const val kotlin = "1.4.10"
    const val glide = "4.9.0"
    const val lifecycleVersion = "2.2.0"
    const val pushTemplates = "1.2.00"
    const val cards = "1.1.00"
    const val pushAmpPlus = "2.1.00"
    const val pushKit = "1.1.00"
    const val hmsPushKit = "4.0.4.301"
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

    const val hmsPushKit = "com.huawei.hms:push:${Versions.hmsPushKit}"

    // logging library
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //glide
    const val glideCore = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    // kotlin libs
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

}