package de.wetterapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Maps OpenWeatherMap icon codes to Material Icons.
 */
fun weatherIcon(iconCode: String): ImageVector = when (iconCode) {
    "01d" -> Icons.Default.WbSunny
    "01n" -> Icons.Default.Nightlight
    "02d", "02n" -> Icons.Default.WbCloudy
    "03d", "03n", "04d", "04n" -> Icons.Default.Cloud
    "09d", "09n" -> Icons.Default.Grain
    "10d", "10n" -> Icons.Default.Umbrella
    "11d", "11n" -> Icons.Default.Thunderstorm
    "13d", "13n" -> Icons.Default.AcUnit
    "50d", "50n" -> Icons.Default.DehazeRounded
    else -> Icons.Default.WbCloudy
}
