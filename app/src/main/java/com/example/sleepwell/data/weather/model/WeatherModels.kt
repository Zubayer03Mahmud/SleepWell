package com.example.sleepwell.data.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("current_weather") val currentWeather: CurrentWeather?,
    val hourly: HourlyData?,
    val daily: DailyData?
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val weathercode: Int,
    val time: String
)

@Serializable
data class HourlyData(
    val time: List<String>,
    @SerialName("temperature_2m") val temperatures: List<Double>,
    @SerialName("weathercode") val weatherCodes: List<Int>,
    @SerialName("relativehumidity_2m") val humidities: List<Int>,
    @SerialName("windspeed_10m") val windSpeeds: List<Double>
)

@Serializable
data class DailyData(
    val time: List<String>,
    @SerialName("weathercode") val weatherCodes: List<Int>,
    @SerialName("temperature_2m_max") val maxTemperatures: List<Double>,
    @SerialName("temperature_2m_min") val minTemperatures: List<Double>,
    @SerialName("precipitation_sum") val precipitationSums: List<Double>
)

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
)
