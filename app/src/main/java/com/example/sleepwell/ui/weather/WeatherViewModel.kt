package com.example.sleepwell.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sleepwell.data.weather.model.City
import com.example.sleepwell.data.weather.model.WeatherResponse
import com.example.sleepwell.data.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _selectedCity = MutableStateFlow(repository.getBangladeshCities().first())
    val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()

    val cities = repository.getBangladeshCities()

    init {
        fetchWeather()
    }

    fun selectCity(city: City) {
        _selectedCity.value = city
        fetchWeather()
    }

    fun refresh() {
        fetchWeather()
    }

    private fun fetchWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val city = _selectedCity.value
                val weather = repository.getWeather(city.latitude, city.longitude)
                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: java.net.UnknownHostException) {
                _uiState.value = WeatherUiState.Error("No internet connection. Please check your network and try again.")
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}
