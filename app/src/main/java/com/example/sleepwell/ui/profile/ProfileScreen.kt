package com.example.sleepwell.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sleepwell.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onLogClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var isDarkMode by remember { mutableStateOf(true) }
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.uploadPhoto(it) }
    }

    val profile = uiState.profile

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
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
            // Error / Success Message
            uiState.errorMessage?.let { error ->
                Text(text = error, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }
            uiState.successMessage?.let { success ->
                Text(text = success, color = Color.Green, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            // Avatar Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(DarkPurple)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (!profile?.profilePhotoUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = profile?.profilePhotoUrl,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = ButtonGradientEnd.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap photo to change",
                color = TextGray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = if (!profile?.fullName.isNullOrBlank()) profile!!.fullName else "SleepWell User",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = profile?.email ?: "",
                color = TextGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            OutlinedButton(
                onClick = { showEditDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ButtonGradientStart)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Edit Profile", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                val memberSince = if (profile != null && profile.createdAt > 0) {
                    SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date(profile.createdAt))
                } else "Jan 2025"

                ProfileInfoRow("Age", if ((profile?.age ?: 0) > 0) "${profile?.age} years" else "Not set")
                ProfileInfoRow("Gender", if (!profile?.gender.isNullOrBlank()) profile!!.gender else "Not set")
                if (!profile?.phoneNumber.isNullOrBlank()) {
                    ProfileInfoRow("Phone", profile!!.phoneNumber)
                }
                ProfileInfoRow("Member Since", memberSince)
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
                        value = "${profile?.targetSleepDuration ?: 8.0} hours",
                        label = "Sleep Duration",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFFFA000)
                    )
                    SleepGoalItem(
                        icon = Icons.Default.Alarm,
                        value = profile?.targetBedtime ?: "10:30 PM",
                        label = "Bedtime",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFEF5350)
                    )
                    SleepGoalItem(
                        icon = Icons.Default.Star,
                        value = "> ${profile?.targetSleepScore ?: 80}",
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
                onClick = {
                    profileViewModel.logout(onLogoutClick)
                },
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

        if (showEditDialog && profile != null) {
            EditProfileDialog(
                currentProfile = profile,
                onDismiss = { showEditDialog = false },
                onSave = { name, age, gender, phone, bio ->
                    profileViewModel.updateProfile(name, age, gender, phone, bio) {
                        showEditDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    currentProfile: com.example.sleepwell.data.model.UserProfile,
    onDismiss: () -> Unit,
    onSave: (fullName: String, age: String, gender: String, phone: String, bio: String) -> Unit
) {
    var fullName by remember { mutableStateOf(currentProfile.fullName) }
    var age by remember { mutableStateOf(if (currentProfile.age > 0) currentProfile.age.toString() else "") }
    var gender by remember { mutableStateOf(currentProfile.gender) }
    var phone by remember { mutableStateOf(currentProfile.phoneNumber) }
    var bio by remember { mutableStateOf(currentProfile.bio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile", color = TextWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name", color = TextGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age", color = TextGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender", color = TextGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number", color = TextGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkPurple,
                        unfocusedContainerColor = DarkPurple,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(fullName, age, gender, phone, bio) }) {
                Text("Save", color = ButtonGradientStart, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextGray)
            }
        },
        containerColor = DarkPurple,
        shape = RoundedCornerShape(24.dp)
    )
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
