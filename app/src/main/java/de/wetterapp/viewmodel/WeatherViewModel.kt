package de.wetterapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import de.wetterapp.BuildConfig
import de.wetterapp.data.model.CurrentWeatherResponse
import de.wetterapp.data.model.DailyForecast
import de.wetterapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data object PermissionRequired : WeatherUiState
    data class Success(
        val current: CurrentWeatherResponse,
        val forecast: List<DailyForecast>,
        val location: Location,
    ) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun onPermissionGranted() {
        loadWeather()
    }

    fun onPermissionDenied() {
        _uiState.value = WeatherUiState.PermissionRequired
    }

    @SuppressLint("MissingPermission")
    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val cancellationToken = CancellationTokenSource()
                val location: Location = fusedLocationClient
                    .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationToken.token)
                    .await()
                    ?: throw IllegalStateException("Standort konnte nicht ermittelt werden.")

                val apiKey = BuildConfig.OWM_API_KEY
                val current = repository.getCurrentWeather(location.latitude, location.longitude, apiKey)
                val forecast = repository.getDailyForecast(location.latitude, location.longitude, apiKey)

                _uiState.value = WeatherUiState.Success(current, forecast, location)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Unbekannter Fehler"
                )
            }
        }
    }
}
