package com.moengage.sampleapp.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.moengage.sampleapp.R
import com.moengage.sampleapp.ui.theme.Grey

@Composable
fun Toolbar() {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
        )

        Image(
            painter = rememberImagePainter(R.drawable.icon_app),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentSize(),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
        )

        Divider(
            thickness = 2.dp,
            color = Grey
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
        )
    }
}