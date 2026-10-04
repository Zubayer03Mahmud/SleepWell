package com.example.sleepwell.ui.log

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sleepwell.ui.theme.*

@Composable
fun AIAnalysisScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    sleepLogViewModel: SleepLogViewModel = viewModel()
) {
    val uiState by sleepLogViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val session = uiState.currentSession ?: remember {
        sleepLogViewModel.generatePrediction()
    }

    val score = session.sleepScore
    val category = session.qualityCategory
    val confidence = session.aiConfidence
    val strengths = if (session.strengths.isNotEmpty()) session.strengths else listOf("Good bedtime consistency", "Low stress levels today", "Adequate physical activity")
    val improvements = if (session.improvements.isNotEmpty()) session.improvements else listOf("High screen time before bed", "Evening caffeine intake detected")

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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "AI Analysis", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Error Message
            uiState.errorMessage?.let { error ->
                Text(text = error, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(bottom = 16.dp))
            }

            // Large Score Ring
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                    Canvas(modifier = Modifier.size(160.dp)) {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.2f),
                            style = Stroke(width = 12.dp.toPx())
                        )
                        drawArc(
                            color = Color(0xFF4CAF50),
                            startAngle = -90f,
                            sweepAngle = (score / 100f) * 360f,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = score.toString(), color = TextWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Text(text = "/ 100", color = TextGray, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = category, color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Based on tonight's data", color = TextGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkPurple.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = ButtonGradientStart, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "AI Confidence: $confidence%", color = ButtonGradientStart, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // AI Analysis Sections
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "AI Analysis", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(text = "STRENGTHS", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    strengths.forEach { strength ->
                        StrengthItem(text = strength)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "NEEDS IMPROVEMENT", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    improvements.forEach { improvement ->
                        ImprovementItem(text = improvement)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tonight's Recommendations
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = DarkPurple
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Tonight's Recommendations", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    RecommendationItem(
                        icon = Icons.Default.NightsStay,
                        title = "Improve Bedtime Routine",
                        pts = "+8 pts expected",
                        iconTint = Color(0xFFFFA000)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RecommendationItem(
                        icon = Icons.Default.Coffee,
                        title = "Reduce Caffeine After 2PM",
                        pts = "+5 pts expected",
                        iconTint = Color(0xFF8D6E63)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    RecommendationItem(
                        icon = Icons.Default.SelfImprovement,
                        title = "5 Min Meditation",
                        pts = "+4 pts expected",
                        iconTint = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Result Button
            Button(
                onClick = {
                    sleepLogViewModel.saveSession(onSuccess = onSaveClick)
                },
                enabled = !uiState.isLoading,
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
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = TextWhite)
                    } else {
                        Text(
                            text = "Save Result",
                            color = TextWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StrengthItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = TextWhite, fontSize = 14.sp)
    }
}

@Composable
fun ImprovementItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, color = TextWhite, fontSize = 14.sp)
    }
}

@Composable
fun RecommendationItem(icon: ImageVector, title: String, pts: String, iconTint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title, 
            color = TextWhite, 
            fontSize = 14.sp,
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
                fontSize = 11.sp, 
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AIAnalysisPreview() {
    SleepWellTheme {
        AIAnalysisScreen(onBackClick = {}, onSaveClick = {})
    }
}
