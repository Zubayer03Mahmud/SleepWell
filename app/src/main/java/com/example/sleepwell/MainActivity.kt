package com.example.sleepwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.data.repository.AuthRepository
import com.example.sleepwell.ui.alarm.AlarmScreen
import com.example.sleepwell.ui.analytics.AnalyticsScreen
import com.example.sleepwell.ui.analytics.WeeklyReportScreen
import com.example.sleepwell.ui.auth.AuthViewModel
import com.example.sleepwell.ui.auth.LoginScreen
import com.example.sleepwell.ui.auth.RegisterScreen
import com.example.sleepwell.ui.chat.AIChatScreen
import com.example.sleepwell.ui.chat.ChatViewModel
import com.example.sleepwell.ui.home.HomeDashboardScreen
import com.example.sleepwell.ui.log.AIAnalysisScreen
import com.example.sleepwell.ui.log.SleepLogScreen
import com.example.sleepwell.ui.log.SleepLogViewModel
import com.example.sleepwell.ui.notifications.NotificationScreen
import com.example.sleepwell.ui.onboarding.OnboardingScreen
import com.example.sleepwell.ui.profile.ProfileScreen
import com.example.sleepwell.ui.profile.ProfileViewModel
import com.example.sleepwell.ui.profile.SettingsScreen
import com.example.sleepwell.ui.theme.SleepWellTheme
import com.example.sleepwell.ui.tips.RecommendationsScreen
import com.example.sleepwell.ui.weather.WeatherScreen

enum class Screen {
    Onboarding, Login, Register, Home, SleepLog, AIAnalysis, Analytics, Recommendations, WeeklyReport, Notifications, Profile, Settings, Weather, Alarm, AIChat
}

class MainActivity : ComponentActivity() {
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SleepWellTheme {
                val startScreen = remember {
                    if (authRepository.isUserLoggedIn()) Screen.Home else Screen.Onboarding
                }
                var currentScreen by remember { mutableStateOf(startScreen) }

                val authViewModel: AuthViewModel = viewModel()
                val sleepLogViewModel: SleepLogViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()
                val chatViewModel: ChatViewModel = viewModel()

                when (currentScreen) {
                    Screen.Onboarding -> {
                        OnboardingScreen(onFinished = {
                            currentScreen = if (authRepository.isUserLoggedIn()) Screen.Home else Screen.Login
                        })
                    }
                    Screen.Login -> {
                        LoginScreen(
                            onLoginClick = { currentScreen = Screen.Home },
                            onRegisterClick = { currentScreen = Screen.Register },
                            onForgotPasswordClick = { },
                            authViewModel = authViewModel
                        )
                    }
                    Screen.Register -> {
                        RegisterScreen(
                            onRegisterClick = { currentScreen = Screen.Home },
                            onBackClick = { currentScreen = Screen.Login },
                            authViewModel = authViewModel
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
                            onAlarmClick = { currentScreen = Screen.Alarm },
                            onChatClick = { currentScreen = Screen.AIChat }
                        )
                    }
                    Screen.SleepLog -> {
                        SleepLogScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            onPredictClick = { currentScreen = Screen.AIAnalysis },
                            sleepLogViewModel = sleepLogViewModel
                        )
                    }
                    Screen.AIAnalysis -> {
                        AIAnalysisScreen(
                            onBackClick = { currentScreen = Screen.SleepLog },
                            onSaveClick = { currentScreen = Screen.Home },
                            sleepLogViewModel = sleepLogViewModel
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
                            onProfileClick = { currentScreen = Screen.Profile },
                            onChatClick = { currentScreen = Screen.AIChat }
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
                            onSettingsClick = { currentScreen = Screen.Settings },
                            profileViewModel = profileViewModel
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
                    Screen.AIChat -> {
                        AIChatScreen(
                            onBackClick = { currentScreen = Screen.Home },
                            chatViewModel = chatViewModel
                        )
                    }
                }
            }
        }
    }
}
