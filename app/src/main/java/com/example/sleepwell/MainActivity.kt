package com.example.sleepwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.analytics.AnalyticsScreen
import com.example.sleepwell.ui.analytics.AnalyticsScreen
import com.example.sleepwell.ui.analytics.WeeklyReportScreen
import com.example.sleepwell.ui.auth.LoginScreen
import com.example.sleepwell.ui.auth.RegisterScreen
import com.example.sleepwell.ui.home.HomeDashboardScreen
import com.example.sleepwell.ui.log.AIAnalysisScreen
import com.example.sleepwell.ui.log.SleepLogScreen
import com.example.sleepwell.ui.notifications.NotificationScreen
import com.example.sleepwell.ui.onboarding.OnboardingScreen
import com.example.sleepwell.ui.profile.ProfileScreen
import com.example.sleepwell.ui.profile.SettingsScreen
import com.example.sleepwell.ui.theme.SleepWellTheme
import com.example.sleepwell.ui.tips.RecommendationsScreen
import com.example.sleepwell.ui.weather.WeatherScreen
import com.example.sleepwell.ui.alarm.AlarmScreen

enum class Screen {
    Onboarding, Login, Register, Home, SleepLog, AIAnalysis, Analytics, Recommendations, WeeklyReport, Notifications, Profile, Settings, Weather, Alarm
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleepWellTheme {
                var currentScreen by remember { mutableStateOf(Screen.Onboarding) }

                when (currentScreen) {
                    Screen.Onboarding -> {
                        OnboardingScreen(onFinished = { currentScreen = Screen.Login })
                    }
                    Screen.Login -> {
                        LoginScreen(
                            onLoginClick = { currentScreen = Screen.Home },
                            onRegisterClick = { currentScreen = Screen.Register },
                            onForgotPasswordClick = { /* Navigate to Forgot Password */ }
                        )
                    }
                    Screen.Register -> {
                        RegisterScreen(
                            onRegisterClick = { currentScreen = Screen.Home },
                            onBackClick = { currentScreen = Screen.Login }
                        )
                    }
                    Screen.Home -> {
                        HomeDashboardScreen(
                            onLogClick = { currentScreen = Screen.SleepLog },
                            onAnalyticsClick = { currentScreen = Screen.Analytics },
                            onRecommendationsClick = { currentScreen = Screen.Recommendations },
                            onNotificationsClick = { currentScreen = Screen.Notifications },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onWeatherClick = { currentScreen = Screen.Weather },
                            onAlarmClick = { currentScreen = Screen.Alarm }
                        )
                    }
                    Screen.SleepLog -> {
                        SleepLogScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            onPredictClick = { currentScreen = Screen.AIAnalysis }
                        )
                    }
                    Screen.AIAnalysis -> {
                        AIAnalysisScreen(
                            onBackClick = { currentScreen = Screen.SleepLog },
                            onSaveClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Analytics -> {
                        AnalyticsScreen(
                            onHomeClick = { currentScreen = Screen.Home },
                            onLogClick = { currentScreen = Screen.SleepLog },
                            onRecommendationsClick = { currentScreen = Screen.Recommendations },
                            onWeeklyReportClick = { currentScreen = Screen.WeeklyReport },
                            onProfileClick = { currentScreen = Screen.Profile }
                        )
                    }
                    Screen.Recommendations -> {
                        RecommendationsScreen(
                            onHomeClick = { currentScreen = Screen.Home },
                            onLogClick = { currentScreen = Screen.SleepLog },
                            onAnalyticsClick = { currentScreen = Screen.Analytics },
                            onProfileClick = { currentScreen = Screen.Profile }
                        )
                    }
                    Screen.WeeklyReport -> {
                        WeeklyReportScreen(
                            onBackClick = { currentScreen = Screen.Analytics }
                        )
                    }
                    Screen.Notifications -> {
                        NotificationScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Profile -> {
                        ProfileScreen(
                            onHomeClick = { currentScreen = Screen.Home },
                            onLogClick = { currentScreen = Screen.SleepLog },
                            onAnalyticsClick = { currentScreen = Screen.Analytics },
                            onNotificationsClick = { currentScreen = Screen.Notifications },
                            onLogoutClick = { currentScreen = Screen.Login },
                            onSettingsClick = { currentScreen = Screen.Settings }
                        )
                    }
                    Screen.Settings -> {
                        SettingsScreen(
                            onBackClick = { currentScreen = Screen.Profile }
                        )
                    }
                    Screen.Weather -> {
                        WeatherScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Alarm -> {
                        AlarmScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                }
            }
        }
    }
}




// Remove LoginPlaceholder

