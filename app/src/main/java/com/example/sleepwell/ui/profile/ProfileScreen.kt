package com.example.sleepwell.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onLogClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var isDarkMode by remember { mutableStateOf(true) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DeepBlue,
                contentColor = TextWhite
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onHomeClick,
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onLogClick,
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                    label = { Text("Log") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onAnalyticsClick,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Analytics") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonGradientStart,
                        selectedTextColor = ButtonGradientStart,
                        indicatorColor = DarkPurple
                    )
                )
            }
        },
        containerColor = DeepBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(DarkPurple),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = ButtonGradientEnd.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Alex Johnson",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "alex@sleepwell.ai",
                color = TextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatItem("82", "Avg Score")
                ProfileStatItem("7d", "Streak")
                ProfileStatItem("47", "Logs")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Profile Info Card
            ProfileSectionCard(title = "Profile Info") {
                ProfileInfoRow("Age", "28 years")
                ProfileInfoRow("Gender", "Male")
                ProfileInfoRow("Member Since", "Jan 2025")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sleep Goals Card
            ProfileSectionCard(title = "Sleep Goals") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleepGoalItem(
                        icon = Icons.Default.NightsStay,
                        value = "8 hours",
                        label = "Sleep Duration",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFFFA000)
                    )
                    SleepGoalItem(
                        icon = Icons.Default.Alarm,
                        value = "10:30 PM",
                        label = "Bedtime",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFEF5350)
                    )
                    SleepGoalItem(
                        icon = Icons.Default.Star,
                        value = "> 80",
                        label = "Sleep Score",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF4FC3F7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Settings Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple.copy(alpha = 0.6f)
            ) {
                Column {
                    SettingsToggleRow(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it }
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        onClick = onNotificationsClick
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Lock,
                        title = "Privacy",
                        onClick = { showPrivacyPolicy = true }
                    )
                    SettingsLinkRow(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        onClick = onSettingsClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Out Button
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign Out", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showPrivacyPolicy) {
            InfoDialog(
                title = "Privacy Policy",
                text = "At SleepWell AI, your privacy is our priority. We only collect sleep data that you manually log or sync from your device to provide personalized AI insights. We do not sell your personal data to third parties. Your data is encrypted and stored securely to help you achieve better sleep health.",
                onDismiss = { showPrivacyPolicy = false }
            )
        }
    }
}

@Composable
fun ProfileStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TextGray, fontSize = 12.sp)
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextGray, fontSize = 14.sp)
        Text(text = value, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SleepGoalItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = DeepBlue.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = TextGray, fontSize = 9.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    SleepWellTheme {
        ProfileScreen({}, {}, {}, {}, {}, {})
    }
}
