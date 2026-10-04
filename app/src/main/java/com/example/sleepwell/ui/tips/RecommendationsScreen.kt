package com.example.sleepwell.ui.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.theme.*

@Composable
fun RecommendationsScreen(
    onHomeClick: () -> Unit,
    onLogClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChatClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

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
                    icon = { Icon(Icons.Default.Lightbulb, contentDescription = null) },
                    label = { Text("Tips") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonGradientStart,
                        selectedTextColor = ButtonGradientStart,
                        indicatorColor = DarkPurple
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onProfileClick,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") }
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
                .padding(24.dp)
        ) {
            Text(
                text = "Recommendations",
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Personalized by AI",
                color = TextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // AI Generated Banner with Chat Action
            Surface(
                onClick = onChatClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(ButtonGradientStart.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💬", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Chat with AI Assistant",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tap to ask custom sleep questions 💬",
                            color = ButtonGradientStart,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Recommendations List
            RecommendationCard(
                icon = Icons.Default.NightsStay,
                title = "Sleep Earlier",
                pts = "+12 pts",
                description = "Aim for lights out by 10:30 PM to align with your circadian rhythm.",
                iconColor = Color(0xFFFFA000)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.Default.Smartphone,
                title = "Reduce Screen Time",
                pts = "+8 pts",
                description = "Put down devices 90 minutes before bed. Blue light delays melatonin release.",
                iconColor = Color(0xFF4FC3F7)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.Default.Coffee,
                title = "Avoid Evening Caffeine",
                pts = "+6 pts",
                description = "Skip coffee and tea after 2 PM. Caffeine has a 6-hour half-life.",
                iconColor = Color(0xFF8D6E63)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.Default.SelfImprovement,
                title = "Practice Meditation",
                pts = "+9 pts",
                description = "A 10-minute mindfulness session can cut sleep onset time by 50%.",
                iconColor = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.AutoMirrored.Filled.DirectionsRun,
                title = "Increase Daily Exercise",
                pts = "+7 pts",
                description = "30 minutes of moderate activity improves sleep quality significantly.",
                iconColor = Color(0xFFFF7043)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.Default.Thermostat,
                title = "Cool Your Room",
                pts = "+5 pts",
                description = "Keep bedroom temperature between 65-68°F for optimal sleep.",
                iconColor = Color(0xFF81D4FA)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RecommendationCard(
                icon = Icons.Default.Alarm,
                title = "Consistent Schedule",
                pts = "+10 pts",
                description = "Wake up at the same time every day to stabilize your internal clock.",
                iconColor = Color(0xFFBA68C8)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RecommendationCard(
    icon: ImageVector,
    title: String,
    pts: String,
    description: String,
    iconColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = DarkPurple
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pts,
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationsPreview() {
    SleepWellTheme {
        RecommendationsScreen(
            onHomeClick = {},
            onLogClick = {},
            onAnalyticsClick = {},
            onProfileClick = {}
        )
    }
}
