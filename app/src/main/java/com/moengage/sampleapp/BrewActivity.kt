package com.moengage.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.moengage.sampleapp.ui.nav.BrewBarNavGraph
import com.moengage.sampleapp.ui.theme.BrewBarTheme
import com.moengage.sampleapp.ui.theme.BrewColors

/**
 * The single Activity. It hosts the NavHost and converts a notification tap into a route:
 * MoEngage campaign extras and `brewbar://` deep links both resolve through
 * `Routes.fromDeeplink`.
 */
class BrewActivity : ComponentActivity() {

    private var pendingDeeplink by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BrewBarTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .consumeWindowInsets(WindowInsets.systemBars),
                    color = BrewColors.PageBackground,
                ) {
                    BrewBarNavGraph(
                        pendingDeeplink = pendingDeeplink,
                        onDeeplinkConsumed = { pendingDeeplink = null },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
