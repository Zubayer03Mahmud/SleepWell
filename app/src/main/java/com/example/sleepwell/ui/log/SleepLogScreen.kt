package com.example.sleepwell.ui.log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun SleepLogScreen(onBackClick: () -> Unit, onPredictClick: () -> Unit) {
    var sleepDuration by remember { mutableFloatStateOf(7f) }
    var bedtime by remember { mutableStateOf("10:30 PM") }
    var wakeTime by remember { mutableStateOf("06:00 AM") }
    var stressLevel by remember { mutableFloatStateOf(4f) }
    var screenTime by remember { mutableStateOf("1-2 hours") }
    var activity by remember { mutableStateOf("Moderate (30 min)") }
    var caffeine by remember { mutableStateOf("1 cup (morning)") }
    var notes by remember { mutableStateOf("") }
    
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "Sleep Log", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Tuesday, July 22", color = TextGray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sleep Duration Section
            Text(text = "Sleep Duration (hours)", color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Visual Bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(DarkPurple)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(sleepDuration / 12f)
                            .fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(ButtonGradientStart, ButtonGradientEnd)))
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "${sleepDuration.toInt()}h", color = ButtonGradientStart, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Slider(
                value = sleepDuration,
                onValueChange = { sleepDuration = it },
                valueRange = 0f..12f,
                colors = SliderDefaults.colors(
                    thumbColor = TextWhite,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bedtime & Wake Up Row
            Row(modifier = Modifier.fillMaxWidth()) {
                TimePickerCard(
                    label = "Bedtime",
                    time = bedtime,
                    icon = Icons.Default.AccessTime,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                TimePickerCard(
                    label = "Wake Up",
                    time = wakeTime,
                    icon = Icons.Default.AccessTime,
                    modifier = Modifier.weight(1f),
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Screen Time
            LogDropdownField(
                label = "Screen Time Before Sleep",
                value = screenTime,
                icon = Icons.Default.Monitor
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Physical Activity
            LogDropdownField(
                label = "Physical Activity",
                value = activity,
                icon = Icons.AutoMirrored.Filled.DirectionsRun
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stress Level
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF9575CD), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Stress Level", color = TextGray, fontSize = 14.sp)
                }
                Text(text = stressLevel.toInt().toString(), color = Color(0xFF9575CD), fontWeight = FontWeight.Bold)
            }
            Slider(
                value = stressLevel,
                onValueChange = { stressLevel = it },
                valueRange = 1f..10f,
                colors = SliderDefaults.colors(
                    thumbColor = TextWhite,
                    activeTrackColor = Color(0xFF9575CD),
                    inactiveTrackColor = DarkPurple
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Caffeine Intake
            LogDropdownField(
                label = "Caffeine Intake",
                value = caffeine,
                icon = Icons.Default.Coffee
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Notes
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Notes", color = TextGray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text(text = "How are you feeling tonight?", color = TextGray, fontSize = 14.sp) },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Predict Button
            Button(
                onClick = onPredictClick,
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Predict My Sleep →",
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
fun TimePickerCard(label: String, time: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NightsStay, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = TextGray, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick() },
            color = DarkPurple
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = time, color = TextWhite, fontWeight = FontWeight.Bold)
                Icon(icon, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun LogDropdownField(label: String, value: String, icon: ImageVector) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = TextGray, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            color = DarkPurple
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value, color = TextWhite)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextGray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SleepLogPreview() {
    SleepWellTheme {
        SleepLogScreen(onBackClick = {}, onPredictClick = {})
    }
}
