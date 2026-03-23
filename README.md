# WetterApp

Android-Wetter-App mit Jetpack Compose.

## Features

- Aktuelles Wetter via GPS-Standort
- 5-Tage-Vorhersage
- Temperatur, Luftfeuchtigkeit, Wind, Sichtweite, Luftdruck
- Niederschlagswahrscheinlichkeit
- Dark Mode Unterstützung

## Einrichtung

### API Key

1. Kostenlosen Account auf [openweathermap.org](https://openweathermap.org) erstellen
2. API Key kopieren
3. In `app/build.gradle.kts` eintragen:

```kotlin
buildConfigField("String", "OWM_API_KEY", "\"DEIN_API_KEY_HIER\"")
```

## Tech Stack

- **Kotlin** + **Jetpack Compose**
- **MVVM** Architektur (ViewModel + StateFlow)
- **Retrofit** + **Gson** für HTTP/JSON
- **FusedLocationProviderClient** für GPS
- **Accompanist Permissions** für Berechtigungsverwaltung
- **Material 3** Design

## Projektstruktur

```
app/src/main/java/de/wetterapp/
├── MainActivity.kt
├── data/
│   ├── model/WeatherModels.kt      # API & UI Datenmodelle
│   ├── remote/
│   │   ├── WeatherApi.kt           # Retrofit Interface
│   │   └── RetrofitClient.kt       # HTTP Client
│   └── repository/
│       └── WeatherRepository.kt    # Datenzugriff & Aggregation
├── ui/
│   ├── components/
│   │   ├── CurrentWeatherCard.kt   # Aktuelle Wetterkarte
│   │   ├── ForecastRow.kt          # Vorhersage-Zeile
│   │   └── WeatherIcon.kt          # Icon-Mapping
│   ├── screens/
│   │   └── WeatherScreen.kt        # Hauptbildschirm
│   └── theme/                      # Material 3 Theme
└── viewmodel/
    └── WeatherViewModel.kt         # State Management
```
