package de.wetterapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.wetterapp.data.model.DailyForecast
import de.wetterapp.ui.theme.SkyBlue
import kotlin.math.roundToInt

@Composable
fun ForecastRow(forecast: DailyForecast, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Day name
        Column(modifier = Modifier.width(90.dp)) {
            Text(
                text = forecast.dayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = forecast.date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }

        Spacer(Modifier.weight(1f))

        // Rain probability
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(52.dp),
        ) {
            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "Niederschlag",
                tint = SkyBlue,
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = "${(forecast.pop * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = SkyBlue,
            )
        }

        // Weather icon
        Icon(
            imageVector = weatherIcon(forecast.icon),
            contentDescription = forecast.description,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(28.dp)
                .padding(horizontal = 4.dp),
        )

        Spacer(Modifier.width(8.dp))

        // Temp range
        Text(
            text = "${forecast.tempMin.roundToInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.width(32.dp),
        )
        Text(
            text = "${forecast.tempMax.roundToInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(32.dp),
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
}
