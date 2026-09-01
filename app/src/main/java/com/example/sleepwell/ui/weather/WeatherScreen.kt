package com.example.sleepwell.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.data.weather.model.WeatherResponse
import com.example.sleepwell.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onBackClick: () -> Unit,
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    var showCityDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Weather", color = TextWhite) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepBlue
                )
            )
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ButtonGradientStart
                    )
                }
                is WeatherUiState.Success -> {
                    WeatherContent(
                        weather = state.weather,
                        cityName = selectedCity.name,
                        onCityClick = { showCityDialog = true }
                    )
                }
                is WeatherUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }

        if (showCityDialog) {
            AlertDialog(
                onDismissRequest = { showCityDialog = false },
                title = { Text("Select City") },
                text = {
                    Column {
                        viewModel.cities.forEach { city ->
                            Text(
                                text = city.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectCity(city)
                                        showCityDialog = false
                                    }
                                    .padding(16.dp),
                                color = if (city == selectedCity) ButtonGradientStart else TextWhite
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCityDialog = false }) {
                        Text("Close")
                    }
                },
                containerColor = DarkPurple,
                titleContentColor = TextWhite,
                textContentColor = TextWhite
            )
        }
    }
}

@Composable
fun WeatherContent(
    weather: WeatherResponse,
    cityName: String,
    onCityClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val current = weather.currentWeather ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // City Selector
        Surface(
            onClick = onCityClick,
            shape = RoundedCornerShape(16.dp),
            color = DarkPurple
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = cityName, color = TextWhite, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main Weather Info
        Icon(
            imageVector = getWeatherIcon(current.weathercode),
            contentDescription = null,
            tint = ButtonGradientStart,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${current.temperature}°C",
            color = TextWhite,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = getWeatherCondition(current.weathercode),
            color = TextGray,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val hourly = weather.hourly
            val humidity = hourly?.humidities?.firstOrNull() ?: 0
            val windSpeed = current.windspeed

            WeatherStatItem(icon = Icons.Default.WaterDrop, value = "$humidity%", label = "Humidity", modifier = Modifier.weight(1f))
            WeatherStatItem(icon = Icons.Default.Air, value = "${windSpeed}km/h", label = "Wind", modifier = Modifier.weight(1f))
            WeatherStatItem(icon = Icons.Default.DeviceThermostat, value = "${current.temperature}°C", label = "Feels like", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Hourly Forecast
        Text(
            text = "Today's Forecast",
            color = TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val hourly = weather.hourly
            if (hourly != null) {
                items(hourly.time.take(24).zip(hourly.temperatures).zip(hourly.weatherCodes)) { pair ->
                    val (timeTemp, code) = pair
                    val (time, temp) = timeTemp
                    HourlyForecastItem(
                        time = time.substringAfter("T"),
                        temp = temp,
                        icon = getWeatherIcon(code)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Daily Forecast
        Text(
            text = "7-Day Forecast",
            color = TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))
        weather.daily?.let { daily ->
            for (i in 0 until daily.time.size) {
                DailyForecastItem(
                    date = daily.time[i],
                    minTemp = daily.minTemperatures[i],
                    maxTemp = daily.maxTemperatures[i],
                    icon = getWeatherIcon(daily.weatherCodes[i]),
                    condition = getWeatherCondition(daily.weatherCodes[i])
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun WeatherStatItem(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkPurple)
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = value, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TextGray, fontSize = 12.sp)
    }
}

@Composable
fun HourlyForecastItem(time: String, temp: Double, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DarkPurple)
            .padding(12.dp)
    ) {
        Text(text = time, color = TextGray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Icon(icon, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${temp}°", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DailyForecastItem(date: String, minTemp: Double, maxTemp: Double, icon: ImageVector, condition: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkPurple,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = date, color = TextWhite, fontWeight = FontWeight.Bold)
                Text(text = condition, color = TextGray, fontSize = 12.sp)
            }
            Icon(icon, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "${maxTemp}° / ${minTemp}°", color = TextWhite, fontWeight = FontWeight.Bold)
        }
    }
}

fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.CloudQueue
        45, 48 -> Icons.Default.FilterDrama
        51, 53, 55, 61, 63, 65 -> Icons.Default.WaterDrop
        71, 73, 75, 77 -> Icons.Default.AcUnit
        80, 81, 82 -> Icons.Default.Umbrella
        95, 96, 99 -> Icons.Default.Thunderstorm
        else -> Icons.Default.Cloud
    }
}

fun getWeatherCondition(code: Int): String {
    return when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
        45, 48 -> "Fog and depositing rime fog"
        51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
        56, 57 -> "Freezing Drizzle: Light and dense intensity"
        61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
        66, 67 -> "Freezing Rain: Light and heavy intensity"
        71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
        85, 86 -> "Snow showers slight and heavy"
        95 -> "Thunderstorm: Slight or moderate"
        96, 99 -> "Thunderstorm with slight and heavy hail"
        else -> "Unknown"
    }
}
