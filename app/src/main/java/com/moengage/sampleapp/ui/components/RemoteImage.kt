package com.moengage.sampleapp.ui.components

import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.moengage.sampleapp.ui.theme.BrewShapes

/**
 * A remote image, for campaign content whose URL only exists at runtime (a card's IMAGE widget).
 *
 * Backed by Glide through [AndroidView] rather than a Compose-native loader: Glide is already a
 * dependency because MoEngage requires it to render in-app images, so this adds no second image
 * stack. Swap the body for Coil or `glide-compose` if this app ever wants a Compose-native one.
 */
@Composable
fun RemoteImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = BrewShapes.iconTile,
) {
    Box(modifier.clip(shape)) {
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { view ->
                view.contentDescription = contentDescription
                Glide.with(view).load(url).centerCrop().into(view)
            },
        )
    }
}
