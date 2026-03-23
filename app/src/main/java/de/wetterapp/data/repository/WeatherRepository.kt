package de.wetterapp.data.repository

import de.wetterapp.data.model.CurrentWeatherResponse
import de.wetterapp.data.model.DailyForecast
import de.wetterapp.data.model.ForecastResponse
import de.wetterapp.data.remote.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherRepository {

    private val api = RetrofitClient.weatherApi

    suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String): CurrentWeatherResponse {
        return api.getCurrentWeather(lat, lon, apiKey)
    }

    suspend fun getDailyForecast(lat: Double, lon: Double, apiKey: String): List<DailyForecast> {
        val response: ForecastResponse = api.getForecast(lat, lon, apiKey)
        return groupForecastByDay(response)
    }

    private fun groupForecastByDay(response: ForecastResponse): List<DailyForecast> {
        val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayNameFormat = SimpleDateFormat("EEEE", Locale("de"))
        val dateDisplayFormat = SimpleDateFormat("dd. MMM", Locale("de"))

        val grouped = response.list.groupBy { item ->
            dayFormat.format(Date(item.dt * 1000))
        }

        return grouped.entries
            .take(5)
            .map { (dateStr, items) ->
                val date = dayFormat.parse(dateStr) ?: Date()
                val tempMin = items.minOf { it.main.tempMin }
                val tempMax = items.maxOf { it.main.tempMax }
                // Noontime entry preferred for description/icon
                val noonItem = items.minByOrNull { item ->
                    val hour = SimpleDateFormat("HH", Locale.getDefault())
                        .format(Date(item.dt * 1000)).toInt()
                    Math.abs(hour - 12)
                } ?: items.first()

                DailyForecast(
                    dayName = dayNameFormat.format(date),
                    date = dateDisplayFormat.format(date),
                    tempMin = tempMin,
                    tempMax = tempMax,
                    description = noonItem.weather.firstOrNull()?.description ?: "",
                    icon = noonItem.weather.firstOrNull()?.icon ?: "01d",
                    pop = items.maxOf { it.pop },
                )
            }
    }
}
