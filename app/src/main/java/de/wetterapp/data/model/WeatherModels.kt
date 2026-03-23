package de.wetterapp.data.model

import com.google.gson.annotations.SerializedName

// ── Current Weather ──────────────────────────────────────────────────────────

data class CurrentWeatherResponse(
    val coord: Coord,
    val weather: List<WeatherCondition>,
    val main: MainWeather,
    val wind: Wind,
    val clouds: Clouds,
    val visibility: Int,
    val name: String,
    val sys: Sys,
    val dt: Long,
)

data class Coord(val lat: Double, val lon: Double)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)

data class MainWeather(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
)

data class Wind(val speed: Double, val deg: Int)

data class Clouds(val all: Int)

data class Sys(
    val country: String?,
    val sunrise: Long,
    val sunset: Long,
)

// ── 5-Day Forecast ───────────────────────────────────────────────────────────

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City,
)

data class ForecastItem(
    val dt: Long,
    val main: MainWeather,
    val weather: List<WeatherCondition>,
    val wind: Wind,
    val clouds: Clouds,
    val visibility: Int,
    @SerializedName("dt_txt") val dtTxt: String,
    val pop: Double,
)

data class City(
    val name: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)

// ── UI Model ─────────────────────────────────────────────────────────────────

data class DailyForecast(
    val dayName: String,
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val description: String,
    val icon: String,
    val pop: Double,
)
