package de.wetterapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.wetterapp.data.model.CurrentWeatherResponse
import de.wetterapp.ui.theme.DeepBlue
import de.wetterapp.ui.theme.SkyBlue
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherCard(
    weather: CurrentWeatherResponse,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val condition = weather.weather.firstOrNull()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(listOf(SkyBlue, DeepBlue)),
                shape = RoundedCornerShape(24.dp),
            )
            .padding(24.dp)
    ) {
        // Refresh button
        IconButton(
            onClick = onRefresh,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Aktualisieren",
                tint = Color.White.copy(alpha = 0.8f),
            )
        }

        Column {
            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${weather.name}, ${weather.sys.country ?: ""}",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(Modifier.height(16.dp))

            // Temperature + Icon
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${weather.main.temp.roundToInt()}°",
                    color = Color.White,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Thin,
                    lineHeight = 80.sp,
                )
                Spacer(Modifier.width(16.dp))
                Icon(
                    imageVector = weatherIcon(condition?.icon ?: "01d"),
                    contentDescription = condition?.description,
                    tint = Color.White,
                    modifier = Modifier.size(64.dp),
                )
            }

            // Description
            Text(
                text = condition?.description?.replaceFirstChar { it.uppercase() } ?: "",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Gefühlt ${weather.main.feelsLike.roundToInt()}°  " +
                        "↑${weather.main.tempMax.roundToInt()}°  " +
                        "↓${weather.main.tempMin.roundToInt()}°",
                color = Color.White.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))

            // Detail row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                WeatherDetail(
                    icon = Icons.Default.Water,
                    label = "Luftfeuchte",
                    value = "${weather.main.humidity}%",
                )
                WeatherDetail(
                    icon = Icons.Default.Air,
                    label = "Wind",
                    value = "${weather.wind.speed.roundToInt()} m/s",
                )
                WeatherDetail(
                    icon = Icons.Default.Visibility,
                    label = "Sicht",
                    value = "${weather.visibility / 1000} km",
                )
                WeatherDetail(
                    icon = Icons.Default.Compress,
                    label = "Druck",
                    value = "${weather.main.pressure} hPa",
                )
            }
        }
    }
}

@Composable
private fun WeatherDetail(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.height(4.dp))
        Text(text = value, color = Color.White, style = MaterialTheme.typography.labelMedium)
        Text(text = label, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelMedium)
    }
}
