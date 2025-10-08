package com.example.letteblack.screens.report

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(navController: NavHostController) {
    val context = LocalContext.current
    var problemDescription by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var problemType by remember { mutableStateOf("Bug Report") }
    var expanded by remember { mutableStateOf(false) }

    val problemTypes = listOf("Bug Report", "App Crash", "Feature Request", "Performance Issue", "Other")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.BugReport,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Column {
                        Text(
                            text = "Help Us Improve",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Report issues and help make the app better",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = problemType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Problem Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    problemTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                problemType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = problemDescription,
                onValueChange = { problemDescription = it },
                label = { Text("Describe the Problem") },
                placeholder = { Text("Please provide details about the issue...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                maxLines = 10,
                shape = RoundedCornerShape(12.dp)
            )

            // Device Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Device Information",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    DeviceInfoRow("Device", "${Build.MANUFACTURER} ${Build.MODEL}")
                    DeviceInfoRow("Android Version", Build.VERSION.RELEASE)
                    DeviceInfoRow("SDK Level", Build.VERSION.SDK_INT.toString())
                    DeviceInfoRow("App Version", "1.0.0")
                }
            }

            Text(
                text = "This information helps us diagnose and fix issues faster.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 1.dp)
            )

           // Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    sendBugReportEmail(
                        context = context,
                        problemType = problemType,
                        userEmail = userEmail,
                        description = problemDescription,
                        deviceInfo = getDeviceInfo()
                    )
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = problemDescription.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Send Report",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.popBackStack()
            },
            icon = {
                Icon(
                    Icons.Default.BugReport,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Thank You!") },
            text = {
                Text("Your report has been prepared. Please select your email app to send it.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    navController.popBackStack()
                }) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun DeviceInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun getDeviceInfo(): String {
    return """
        Device: ${Build.MANUFACTURER} ${Build.MODEL}
        Android Version: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})
        Device Brand: ${Build.BRAND}
        Device: ${Build.DEVICE}
        Product: ${Build.PRODUCT}
    """.trimIndent()
}

fun sendBugReportEmail(
    context: android.content.Context,
    problemType: String,
    userEmail: String,
    description: String,
    deviceInfo: String
) {
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("lettrblack@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "$problemType - App Issue Report")

        val emailBody = buildString {
            append("Problem Type: $problemType\n\n")
            if (userEmail.isNotBlank()) {
                append("User Email: $userEmail\n\n")
            }
            append("Description:\n")
            append("$description\n\n")
            append("------------\n")
            append("Device Information:\n")
            append(deviceInfo)
        }

        putExtra(Intent.EXTRA_TEXT, emailBody)
    }

    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send report via..."))
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Please install an email app.",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
