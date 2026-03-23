package de.wetterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import de.wetterapp.ui.screens.WeatherScreen
import de.wetterapp.ui.theme.WetterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WetterAppTheme {
                WeatherScreen(modifier = Modifier.safeDrawingPadding())
            }
        }
    }
}
