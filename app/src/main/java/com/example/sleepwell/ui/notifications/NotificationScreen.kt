package com.example.sleepwell.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.theme.*

@Composable
fun NotificationScreen(onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

    var pushEnabled by remember { mutableStateOf(true) }
    var bedtimeEnabled by remember { mutableStateOf(true) }
    var wakeupEnabled by remember { mutableStateOf(true) }
    var dailyLogEnabled by remember { mutableStateOf(false) }
    var meditationEnabled by remember { mutableStateOf(true) }
    var weeklyReportEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepBlue)
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
                        text = "Notifications",
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Manage your reminders",
                        color = TextGray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Master Toggle
            NotificationItem(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                subtitle = "3 of 5 reminders active",
                checked = pushEnabled,
                onCheckedChange = { pushEnabled = it },
                iconColor = Color(0xFFFFA000)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = TextGray.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Reminders List
            NotificationItem(
                icon = Icons.Default.NightsStay,
                title = "Bedtime Reminder",
                subtitle = "Time to start winding down\n10:00 PM",
                checked = bedtimeEnabled,
                onCheckedChange = { bedtimeEnabled = it },
                iconColor = Color(0xFF90CAF9)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationItem(
                icon = Icons.Default.WbSunny,
                title = "Wake-up Reminder",
                subtitle = "Good morning alarm\n6:30 AM",
                checked = wakeupEnabled,
                onCheckedChange = { wakeupEnabled = it },
                iconColor = Color(0xFFFFD54F)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationItem(
                icon = Icons.Default.Description,
                title = "Daily Log Reminder",
                subtitle = "Log yesterday's sleep\n7:00 AM",
                checked = dailyLogEnabled,
                onCheckedChange = { dailyLogEnabled = it },
                iconColor = Color(0xFFAED581)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationItem(
                icon = Icons.Default.SelfImprovement,
                title = "Meditation Reminder",
                subtitle = "Evening relaxation\n9:30 PM",
                checked = meditationEnabled,
                onCheckedChange = { meditationEnabled = it },
                iconColor = Color(0xFFCE93D8)
            )

            Spacer(modifier = Modifier.height(16.dp))

            NotificationItem(
                icon = Icons.Default.Assessment,
                title = "Weekly Report",
                subtitle = "Your weekly sleep summary\nEvery Sunday",
                checked = weeklyReportEnabled,
                onCheckedChange = { weeklyReportEnabled = it },
                iconColor = Color(0xFF80CBC4)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun NotificationItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = DarkPurple.copy(alpha = 0.6f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = TextGray,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextWhite,
                    checkedTrackColor = ButtonGradientStart,
                    uncheckedThumbColor = TextGray,
                    uncheckedTrackColor = DarkPurple,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    SleepWellTheme {
        NotificationScreen(onBackClick = {})
    }
}
