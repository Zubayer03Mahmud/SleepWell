package com.example.sleepwell.ui.analytics

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.ui.theme.*

@Composable
fun AnalyticsScreen(
    onHomeClick: () -> Unit,
    onLogClick: () -> Unit,
    onRecommendationsClick: () -> Unit,
    onWeeklyReportClick: () -> Unit,
    onProfileClick: () -> Unit,
    analyticsViewModel: AnalyticsViewModel = viewModel()
) {
    val uiState by analyticsViewModel.uiState.collectAsState()
    var selectedPeriod by remember { mutableStateOf("This Week") }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        analyticsViewModel.loadAnalytics()
    }

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
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                    label = { Text("Analytics") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ButtonGradientStart,
                        selectedTextColor = ButtonGradientStart,
                        indicatorColor = DarkPurple
                    )
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
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Analytics",
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Your sleep insights",
                color = TextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Time Period Selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkPurple)
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    TimePeriodButton(
                        text = "This Week",
                        isSelected = selectedPeriod == "This Week",
                        onClick = { selectedPeriod = "This Week" },
                        modifier = Modifier.weight(1f)
                    )
                    TimePeriodButton(
                        text = "This Month",
                        isSelected = selectedPeriod == "This Month",
                        onClick = { selectedPeriod = "This Month" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sleep Score Main Chart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sleep Score", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onWeeklyReportClick) {
                    Text(text = "View all →", color = ButtonGradientStart, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                SleepScoreAreaChart(points = uiState.chartPoints)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Report Button
            Button(
                onClick = onWeeklyReportClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(ButtonGradientStart, ButtonGradientEnd)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Weekly Report",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Mini Charts Row
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = DarkPurple
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Duration (hrs)", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        DurationBarChart()
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = DarkPurple
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Stress Level", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        StressLineChart()
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stat Cards Grid
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnalyticsStatCard(
                        icon = Icons.Default.NightsStay,
                        value = uiState.avgSleepDuration,
                        label = "Avg Sleep",
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFFFFA000)
                    )
                    AnalyticsStatCard(
                        icon = Icons.Default.Star,
                        value = uiState.bestDay,
                        label = "Best Day",
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFFFFD700)
                    )
                    AnalyticsStatCard(
                        icon = Icons.Default.MoodBad,
                        value = uiState.worstDay,
                        label = "Worst Day",
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFFEF5350)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnalyticsStatCard(
                        icon = Icons.Default.Psychology,
                        value = uiState.avgStressLevel,
                        label = "Avg Stress",
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFF9575CD)
                    )
                    AnalyticsStatCard(
                        icon = Icons.Default.Assessment,
                        value = uiState.avgSleepScore,
                        label = "Avg Score",
                        modifier = Modifier.weight(1f),
                        iconTint = ButtonGradientStart
                    )
                    AnalyticsStatCard(
                        icon = Icons.Default.Whatshot,
                        value = uiState.streakDays,
                        label = "Streak",
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFFFF7043)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TimePeriodButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) ButtonGradientStart else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) TextWhite else TextGray,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SleepScoreAreaChart(points: List<Float> = listOf(0.5f, 0.7f, 0.6f, 0.8f, 0.5f, 0.9f, 0.75f)) {
    val textGrayArgb = TextGray.toArgb()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    
    Canvas(modifier = Modifier.fillMaxSize().padding(start = 40.dp, end = 20.dp, top = 20.dp, bottom = 40.dp)) {
        val width = size.width
        val height = size.height
        val stepX = width / (points.size - 1).coerceAtLeast(1)

        // Draw Grid and Y-Axis Labels
        val paint = Paint().apply {
            color = textGrayArgb
            textAlign = Paint.Align.RIGHT
            textSize = 10.dp.toPx()
        }
        
        val yLabels = listOf("100", "50", "0")
        yLabels.forEach { label ->
            val y = height - (label.toFloat() / 100f * height)
            drawContext.canvas.nativeCanvas.drawText(label, -10.dp.toPx(), y + 4.dp.toPx(), paint)
            drawLine(
                color = TextGray.copy(alpha = 0.1f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        val path = Path()
        val fillPath = Path()

        points.forEachIndexed { index, yPercent ->
            val x = index * stepX
            val y = height - (yPercent * height)
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            // X-Axis labels
            val dayLabel = days.getOrElse(index % days.size) { "" }
            drawContext.canvas.nativeCanvas.drawText(
                dayLabel,
                x,
                height + 24.dp.toPx(),
                Paint().apply {
                    color = textGrayArgb
                    textAlign = Paint.Align.CENTER
                    textSize = 10.dp.toPx()
                }
            )

            if (index == points.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(ButtonGradientStart.copy(alpha = 0.3f), Color.Transparent)
            )
        )

        drawPath(
            path = path,
            color = ButtonGradientStart,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
        
        // Dots
        points.forEachIndexed { index, yPercent ->
            val x = index * stepX
            val y = height - (yPercent * height)
            drawCircle(color = TextWhite, radius = 4.dp.toPx(), center = Offset(x, y))
            drawCircle(color = ButtonGradientStart, radius = 2.dp.toPx(), center = Offset(x, y))
        }
    }
}

@Composable
fun DurationBarChart() {
    val textGrayArgb = TextGray.toArgb()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    
    Canvas(modifier = Modifier.fillMaxSize().padding(bottom = 20.dp)) {
        val data = listOf(0.6f, 0.8f, 0.5f, 0.9f, 0.7f, 0.85f, 0.6f)
        val barWidth = 6.dp.toPx()
        val chartHeight = size.height - 20.dp.toPx()
        val spacing = (size.width - (data.size * barWidth)) / (data.size + 1)
        
        data.forEachIndexed { index, value ->
            val x = spacing + index * (barWidth + spacing)
            val barHeight = value * chartHeight
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(ButtonGradientStart, ButtonGradientEnd)),
                topLeft = Offset(x, chartHeight - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
            
            drawContext.canvas.nativeCanvas.drawText(
                days[index].take(1),
                x + barWidth / 2,
                size.height,
                Paint().apply {
                    color = textGrayArgb
                    textAlign = Paint.Align.CENTER
                    textSize = 8.sp.toPx()
                }
            )
        }
    }
}

@Composable
fun StressLineChart() {
    val textGrayArgb = TextGray.toArgb()
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    
    Canvas(modifier = Modifier.fillMaxSize().padding(bottom = 20.dp)) {
        val points = listOf(0.4f, 0.6f, 0.3f, 0.5f, 0.7f, 0.4f, 0.5f)
        val chartHeight = size.height - 20.dp.toPx()
        val stepX = size.width / (points.size - 1)
        val path = Path()
        
        points.forEachIndexed { index, value ->
            val x = index * stepX
            val y = chartHeight - (value * chartHeight)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            
            drawContext.canvas.nativeCanvas.drawText(
                days[index].take(1),
                x,
                size.height,
                Paint().apply {
                    color = textGrayArgb
                    textAlign = Paint.Align.CENTER
                    textSize = 8.sp.toPx()
                }
            )
        }
        
        drawPath(
            path = path,
            color = Color(0xFFFFB74D),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun AnalyticsStatCard(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier, iconTint: Color) {
    Surface(
        modifier = modifier.heightIn(min = 90.dp),
        shape = RoundedCornerShape(16.dp),
        color = DarkPurple
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value, 
                color = TextWhite, 
                fontSize = 15.sp, 
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = label, 
                color = TextGray, 
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsPreview() {
    SleepWellTheme {
        AnalyticsScreen(
            onHomeClick = {},
            onLogClick = {},
            onRecommendationsClick = {},
            onWeeklyReportClick = {},
            onProfileClick = {}
        )
    }
}
