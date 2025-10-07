package com.example.letteblack.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.letteblack.AuthViewModel
import com.example.letteblack.UserState
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.settings.SettingsSection
import com.example.letteblack.screens.settings.datastore.SettingDataStore
import com.example.letteblack.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var sounds by remember { mutableStateOf(true) }
    var notificationDialog by remember {mutableStateOf(false)}

    val userViewModal: UserViewModel = hiltViewModel()

    val context: Context = LocalContext.current
    val dataStore = remember { SettingDataStore(context) }
    val scope = rememberCoroutineScope()
    val notificationEnabled by dataStore.notificationPreference.collectAsState(initial = true)

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
                onClick = {navController.navigate(Routes.Account.toString())})
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
            SettingsSection("Sounds") {
                SwitchSettingItem(
                    icon = Icons.Default.VolumeUp,
                    title = "App Sounds",
                    checked = sounds,
                    onCheckedChange = { sounds = it }
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout", color = MaterialTheme.colorScheme.onError)
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
                        TextButton(onClick = {notificationDialog =false}) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
