package com.example.sleepwell.data.weather.repository

import com.example.sleepwell.data.weather.api.WeatherApiService
import com.example.sleepwell.data.weather.model.City
import com.example.sleepwell.data.weather.model.WeatherResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class WeatherRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val apiService = retrofit.create(WeatherApiService::class.java)

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherResponse {
        return apiService.getWeather(latitude, longitude)
    }

    fun getBangladeshCities(): List<City> {
        return listOf(
            City("Dhaka", 23.8103, 90.4125),
            City("Chattogram", 22.3569, 91.7832),
            City("Sylhet", 24.8949, 91.8687),
            City("Rajshahi", 24.3745, 88.6042),
            City("Khulna", 22.8456, 89.5403),
            City("Barishal", 22.7010, 90.3535),
            City("Rangpur", 25.7439, 89.2752),
            City("Mymensingh", 24.7471, 90.4203)
        )
    }
}
