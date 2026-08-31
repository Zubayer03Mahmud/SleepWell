package com.example.sleepwell.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.theme.*

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()
    var isDarkMode by remember { mutableStateOf(value = true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlue),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
                .systemBarsPadding()
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Settings",
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize your experience",
                        color = TextGray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // APPEARANCE Section
            SettingsSectionHeader("APPEARANCE")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple.copy(alpha = 0.6f)
            ) {
                Column {
                    SettingsToggleRow(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        checked = isDarkMode
                    ) { isDarkMode = it }
                    SettingsLinkRow(
                        icon = Icons.Default.Language,
                        title = "Language",
                        value = "English"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SLEEP Section
            SettingsSectionHeader("SLEEP")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple.copy(alpha = 0.6f)
            ) {
                Column {
                    SettingsLinkRow(
                        icon = Icons.Default.NightsStay,
                        title = "Sleep Goal",
                        value = "8 hours"
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Sync,
                        title = "Data Sync",
                        value = "On"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PRIVACY Section
            SettingsSectionHeader("PRIVACY")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple.copy(alpha = 0.6f)
            ) {
                Column {
                    SettingsLinkRow(
                        icon = Icons.Default.Lock,
                        title = "Privacy Policy"
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Description,
                        title = "Terms of Service"
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Delete,
                        title = "Delete Account",
                        titleColor = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ABOUT Section
            SettingsSectionHeader("ABOUT")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple.copy(alpha = 0.6f)
            ) {
                Column {
                    SettingsLinkRow(
                        icon = Icons.Default.Info,
                        title = "About SleepWell AI"
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Star,
                        title = "Rate the App"
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Email,
                        title = "Send Feedback"
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Version Info
            Text(
                text = "SleepWell AI v2.4.1",
                color = TextGray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "© 2025 SleepWell Technologies",
                color = TextGray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = TextGray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsLinkRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    titleColor: Color = TextWhite,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, color = titleColor, fontSize = 16.sp, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(text = value, color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    SleepWellTheme {
        SettingsScreen(onBackClick = {})
    }
}
