package com.example.sleepwell.ui.analytics

import androidx.compose.foundation.Canvas
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
fun WeeklyReportScreen(onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()

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
                Text(
                    text = "Weekly Report",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Weekly Average Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.2f),
                            style = Stroke(width = 8.dp.toPx())
                        )
                        drawArc(
                            color = ButtonGradientStart,
                            startAngle = -90f,
                            sweepAngle = 270f, // 75/100
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "75", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(text = "/ 100", color = TextGray, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column {
                    Text(text = "WEEKLY AVERAGE", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Good progress!", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = ActiveDot,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+5 vs last week", color = ActiveDot, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Sleep Trend Chart
            Text(text = "Sleep Trend", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Box(modifier = Modifier.padding(20.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val points = listOf(0.6f, 0.4f, 0.7f, 0.5f, 0.8f, 0.7f) // Tue to Sun
                        val stepX = size.width / (points.size - 1)
                        val path = Path()
                        
                        points.forEachIndexed { index, value ->
                            val x = index * stepX
                            val y = size.height - (value * size.height)
                            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        
                        drawPath(
                            path = path,
                            color = ButtonGradientStart,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
            // Chart Labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                    Text(text = it, color = TextGray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // AI Feedback Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = null,
                            tint = Color(0xFF9575CD),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "AI Feedback", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Great week, Alex! Your bedtime consistency improved by 28% and stress levels dropped notably. Focus on reducing screen time this coming week to push past the 80-point mark.",
                        color = TextGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Achievements Section
            Text(text = "Achievements", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            
            AchievementItem(
                icon = Icons.Default.Whatshot,
                title = "7-Day Streak",
                description = "Logged sleep every day",
                iconColor = Color(0xFFFF7043)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            AchievementItem(
                icon = Icons.Default.Alarm,
                title = "Improved Bedtime",
                description = "Consistent by 30 min",
                iconColor = Color(0xFFBA68C8)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AchievementItem(
                icon = Icons.Default.WbSunny,
                title = "Early Bird",
                description = "Consistent wake up time",
                iconColor = Color(0xFFFFD54F)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AchievementItem(
                icon = Icons.Default.NightsStay,
                title = "Deep Sleeper",
                description = "75% deep sleep cycles",
                iconColor = Color(0xFF4FC3F7)
            )

            Spacer(modifier = Modifier.height(12.dp))

            AchievementItem(
                icon = Icons.Default.Smartphone,
                title = "Screen Free",
                description = "No devices before bed",
                iconColor = Color(0xFF81C784)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AchievementItem(
    icon: ImageVector,
    title: String,
    description: String,
    iconColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = DarkPurple
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
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
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = description, color = TextGray, fontSize = 12.sp)
            }
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Earned",
                tint = Color(0xFF4CAF50).copy(alpha = 0.3f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyReportPreview() {
    SleepWellTheme {
        WeeklyReportScreen(onBackClick = {})
    }
}
