package de.wetterapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import de.wetterapp.ui.components.CurrentWeatherCard
import de.wetterapp.ui.components.ForecastRow
import de.wetterapp.viewmodel.WeatherUiState
import de.wetterapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    ) { permissionResults ->
        if (permissionResults.values.any { it }) {
            viewModel.onPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        if (locationPermissions.allPermissionsGranted) {
            viewModel.onPermissionGranted()
        } else {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading -> LoadingScreen()
            is WeatherUiState.PermissionRequired -> PermissionScreen {
                locationPermissions.launchMultiplePermissionRequest()
            }
            is WeatherUiState.Error -> ErrorScreen(state.message) {
                viewModel.loadWeather()
            }
            is WeatherUiState.Success -> SuccessScreen(state, viewModel::loadWeather)
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Wetterdaten werden geladen…")
        }
    }
}

@Composable
private fun PermissionScreen(onRequest: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Standortzugriff benötigt",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Die App benötigt Zugriff auf deinen Standort, um das aktuelle Wetter anzeigen zu können.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRequest) {
                Text("Berechtigung erteilen")
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Fehler beim Laden",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onRetry) {
                Text("Nochmal versuchen")
            }
        }
    }
}

@Composable
private fun SuccessScreen(
    state: WeatherUiState.Success,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Spacer(Modifier.height(8.dp))

        CurrentWeatherCard(
            weather = state.current,
            onRefresh = onRefresh,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "5-Tage-Vorhersage",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 4.dp),
        )

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                state.forecast.forEachIndexed { index, daily ->
                    ForecastRow(daily)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}
