# Weather Feature Walkthrough

I have successfully implemented the Weather feature in the SleepWell application.

## Key Accomplishments

- **API Integration**: Used **Open-Meteo API**, a free and reliable weather service that doesn't require an API key.
- **Bangladeshi Locations**: Added support for 8 major cities in Bangladesh (Dhaka, Chattogram, Sylhet, Rajshahi, Khulna, Barishal, Rangpur, Mymensingh) with their exact coordinates.
- **MVVM Architecture**: Followed the project's existing pattern with a dedicated Repository and ViewModel.
- **UI Design**: Built a modern, dark-themed Weather screen using Jetpack Compose that matches the SleepWell branding.
- **Comprehensive Data**:
    - Current temperature and weather conditions.
    - "Feels like", Humidity, and Wind speed stats.
    - 24-hour hourly forecast.
    - 7-day daily forecast.
- **User Interactions**:
    - Manual city selection via an easy-to-use dialog.
    - Refresh functionality to get the latest data.
    - Seamless navigation from the Home Dashboard.

## Technical Details

### Dependencies Added
- `Retrofit`: For network requests.
- `Kotlinx Serialization`: For JSON parsing.
- `ViewModel Compose`: For lifecycle-aware UI state management.
- `Coil`: For image loading (ready for weather icons if needed, though using Material Icons for consistency).

### Permissions
- Added `INTERNET`, `ACCESS_COARSE_LOCATION`, and `ACCESS_FINE_LOCATION` to `AndroidManifest.xml`.

### Data Flow
1. **API**: `WeatherApiService` defines the Open-Meteo endpoints.
2. **Repository**: `WeatherRepository` manages the Retrofit instance and provides Bangladeshi city data.
3. **ViewModel**: `WeatherViewModel` fetches weather data for the selected city and exposes a `WeatherUiState`.
4. **UI**: `WeatherScreen` observes the state and renders the content, including a city selector.
5. **Navigation**: `MainActivity` manages the transition between Home and Weather screens.

## How to Test

1. **Launch the App**: The app starts at the Onboarding screen.
2. **Navigate to Home**: Skip or finish the login/registration process to reach the Home Dashboard.
3. **Open Weather**: Look for the new "WEATHER" card on the Home screen and tap it.
4. **Verify Data**: The screen should load weather for **Dhaka** by default.
5. **Change City**: Tap the city name at the top to select a different city like "Sylhet" or "Chattogram".
6. **Check Forecast**: Scroll down to see today's hourly forecast and the 7-day outlook.
7. **Refresh**: Tap the refresh icon in the top bar to update the data.
