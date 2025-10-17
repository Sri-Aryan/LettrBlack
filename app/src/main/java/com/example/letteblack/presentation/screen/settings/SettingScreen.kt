package com.example.letteblack.presentation.screen.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.letteblack.data.datastore.SettingDataStore
import com.example.letteblack.data.remote.Routes
import com.example.letteblack.screens.settings.SettingsSection
import com.example.letteblack.viewmodel.AuthViewModel
import com.example.letteblack.viewmodel.UserState
import com.example.letteblack.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel) {

    val userViewModal: UserViewModel = hiltViewModel()

    val context: Context = LocalContext.current
    val dataStore = remember { SettingDataStore(context) }
    val scope = rememberCoroutineScope()
    val notificationEnabled by dataStore.notificationPreference.collectAsState(initial = true)
    val soundEnabled by dataStore.soundPrefrences.collectAsState(initial = true)
    var soundsDialog by remember { mutableStateOf(false) }
    var notificationDialog by remember {mutableStateOf(false)}

    // React to logout
    LaunchedEffect(authViewModel.userState.value) {
        if (authViewModel.userState.value is UserState.Unauthenticated) {
            navController.navigate(Routes.Login.toString()) {
                popUpTo(Routes.Login.toString()) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Account Section
            SettingsSection("Account") {
                SettingsItem(Icons.Default.Person, "Profile", "Manage your account",
                onClick = {navController.navigate("profile")})
                SettingsItem(Icons.Default.Lock, "Privacy", "Control your privacy",
                    onClick = { navController.navigate("privacy") })
            }
            // Premium Section
            SettingsSection("Premium") {
                SettingsItem(Icons.Default.Star, "Upgrade", "Unlock premium courses",
                    onClick = {navController.navigate("premium")})
            }
            // Notifications
            SettingsSection("Notifications") {
                SwitchSettingItem(
                    icon = Icons.Default.Notifications,
                    title = "Enable Notifications",
                    checked = notificationEnabled,
                    onCheckedChange = { isChecked ->
                        if (!isChecked) {
                            notificationDialog = true
                        } else {
                            scope.launch { dataStore.savedNotificationPreference(true) }
                            userViewModal.setNotificationEnabled(isChecked)
                        }
                    }
                )
            }

            // Sounds

            //Whenever using sounds then implement Playing sound using this value
            /*
            Ex:
            val user = userViewModel.user.value
                if (user?.soundEnabled == true) {
                    mediaPlayer.start()
                }
            */
            SettingsSection("Sounds") {
                SwitchSettingItem(
                    icon = Icons.Default.VolumeUp,
                    title = "App Sounds",
                    checked = soundEnabled,
                    onCheckedChange = { isChecked->
                        if (!isChecked) {
                            soundsDialog = true
                        } else {
                            scope.launch { dataStore.savedSoundPrefrence(true) }
                            userViewModal.setSoundEnabled(isChecked)
                        }
                    }
                )
            }
            // Help & Report
            SettingsSection("Support") {
                SettingsItem(
                    Icons.Default.Help,
                    "Help",
                    "Get app guidance")
                SettingsItem(
                    Icons.Default.BugReport,
                    "Report",
                    "Report a problem",
                    onClick = {navController.navigate("report")})
            }

            Spacer(modifier = Modifier.weight(1f))
            // Logout
            Button(
                onClick = { authViewModel.signOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 102.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E19A9))
            ) {
                Text("Logout", color = Color.White)
            }

            // Version Info
            Text(
                text = "Version 1.0.0",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp, bottom = 4.dp)
            )

            //Handling NotificationDialog
            if(notificationDialog){
                AlertDialog(
                    onDismissRequest = {notificationDialog = false},
                    title = {Text("Notification")},
                    text = {
                        Text("You will miss important updates and reminders."+
                                "Are you sure you want to disable notifications?"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch { dataStore.savedNotificationPreference(false) }
                            userViewModal.setNotificationEnabled(false)
                            notificationDialog = false
                        }) {
                            Text("Yes", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {notificationDialog = false}) {
                            Text("Cancel")
                        }
                    }
                )
            }
            if (soundsDialog) {
                AlertDialog(
                    onDismissRequest = { soundsDialog = false },
                    title = { Text("Turn off sounds?") },
                    text = { Text("You won’t hear button clicks or notifications in the app.") },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch { dataStore.savedSoundPrefrence(false) }
                            userViewModal.setSoundEnabled(false)
                            soundsDialog = false
                        }) { Text("Yes", color = MaterialTheme.colorScheme.error) }
                    },
                    dismissButton = {
                        TextButton(onClick = { soundsDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

//
//@Composable
//fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleMedium,
//            color = MaterialTheme.colorScheme.primary
//        )
//        Surface(
//            shape = MaterialTheme.shapes.medium,
//            tonalElevation = 2.dp,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.padding(8.dp)) {
//                content()
//            }
//        }
//    }
//}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = Color(0xFF7E19A9).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = Color(0xFF7E19A9),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun SwitchSettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = Color(0xFF7E19A9).copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = Color(0xFF7E19A9),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF7E19A9),
                checkedTrackColor = Color(0xFF7E19A9).copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.LightGray
            )
        )

    }
}
