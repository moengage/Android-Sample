package com.moengage.example.ordertracking.render

import android.content.Context
import androidx.core.graphics.drawable.IconCompat
import com.moengage.example.R

/** Maps dashboard icon keys (e.g. `"scooter"`) to vector drawables bundled in the sample app. */
internal fun notificationIcon(context: Context, key: String): IconCompat {
    val resId =
        when (key) {
            "receipt" -> R.drawable.order_tracking_ic_receipt
            "chef" -> R.drawable.order_tracking_ic_chef
            "helmet" -> R.drawable.order_tracking_ic_helmet
            "scooter" -> R.drawable.order_tracking_ic_scooter
            "plate" -> R.drawable.order_tracking_ic_plate
            "restaurant" -> R.drawable.order_tracking_ic_restaurant
            "home" -> R.drawable.order_tracking_ic_home
            else -> R.drawable.order_tracking_ic_receipt
        }
    return IconCompat.createWithResource(context, resId)
}
