package com.example.sleepwell.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sleepwell.ui.theme.*

@Composable
fun HomeDashboardScreen(
    onLogClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onRecommendationsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DeepBlue,
                contentColor = TextWhite
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonGradientStart,
                        selectedTextColor = ButtonGradientStart,
                        indicatorColor = DarkPurple
                    )
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
                    selected = false,
                    onClick = onRecommendationsClick,
                    icon = { Icon(Icons.Default.Lightbulb, contentDescription = null) },
                    label = { Text("Tips") }
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Good evening,", color = TextGray, fontSize = 14.sp)
                    Text(
                        text = "Alex Johnson 👋",
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkPurple)
                    ) {
                        Box {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = TextWhite)
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkPurple)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = TextWhite)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sleep Score Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                        Canvas(modifier = Modifier.size(100.dp)) {
                            drawCircle(
                                color = Color.Gray.copy(alpha = 0.2f),
                                style = Stroke(width = 8.dp.toPx())
                            )
                            drawArc(
                                color = Color(0xFF4CAF50),
                                startAngle = -90f,
                                sweepAngle = 295f, // Approx 82/100
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "82", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            Text(text = "/ 100", color = TextGray, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Column {
                        Text(text = "TODAY'S SLEEP SCORE", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Excellent", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ActiveDot.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "↑ 7 pts vs last night", color = ActiveDot, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Prediction Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "AI PREDICTION", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "94% confidence", color = ActiveDot, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Tonight's Forecast", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Excellent Sleep Expected", color = TextWhite, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Tap to see full analysis →", color = TextGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Stats
            Text(text = "Quick Stats", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(icon = Icons.Default.NightsStay, value = "7.5h", label = "Duration")
                StatItem(icon = Icons.Default.SentimentNeutral, value = "3/10", label = "Stress")
                StatItem(icon = Icons.Default.Smartphone, value = "1.2h", label = "Screen")
                StatItem(icon = Icons.Default.Coffee, value = "1 cup", label = "Caffeine")
                StatItem(icon = Icons.Default.DirectionsRun, value = "8,420", label = "Activity")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 7-Day Trend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "7-Day Trend", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = { }) {
                    Text(text = "View all →", color = ButtonGradientStart, fontSize = 14.sp)
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 20.dp)) {
                    val path = Path()
                    val points = listOf(0.6f, 0.4f, 0.7f, 0.5f, 0.8f, 0.6f, 0.9f)
                    val width = size.width
                    val height = size.height
                    
                    points.forEachIndexed { index, yPos ->
                        val x = index * (width / (points.size - 1))
                        val y = height - (yPos * height)
                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                    }
                    
                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(listOf(ButtonGradientStart, ButtonGradientEnd)),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Today's Tips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Today's Tips", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onRecommendationsClick) {
                    Text(text = "See all →", color = ButtonGradientStart, fontSize = 14.sp)
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.NightsStay, contentDescription = null, tint = Color(0xFFFFA000))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Sleep 30 minutes earlier", color = TextWhite, fontSize = 14.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DarkPurple)
            .padding(8.dp)
            .width(56.dp)
    ) {
        Icon(icon, contentDescription = null, tint = ButtonGradientEnd, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = TextGray, fontSize = 10.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeDashboardPreview() {
    SleepWellTheme {
        HomeDashboardScreen(
            onLogClick = {},
            onAnalyticsClick = {},
            onRecommendationsClick = {},
            onNotificationsClick = {},
            onProfileClick = {}
        )
    }
}
