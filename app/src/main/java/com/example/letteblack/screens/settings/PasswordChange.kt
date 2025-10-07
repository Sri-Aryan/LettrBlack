package com.example.letteblack.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    navController: NavHostController,
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var currentPasswordError by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Password Requirements",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        "• At least 8 characters\n• Contains uppercase and lowercase\n• Contains at least one number\n• Contains at least one special character",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            PasswordSection("Verification") {
                // Current Password
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        currentPasswordError = ""
                    },
                    label = { Text("Current Password") },
                    visualTransformation = if (currentPasswordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                            Icon(
                                imageVector = if (currentPasswordVisible)
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (currentPasswordVisible)
                                    "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = currentPasswordError.isNotEmpty(),
                    supportingText = if (currentPasswordError.isNotEmpty()) {
                        { Text(currentPasswordError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )

                // New Password
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        newPasswordError = ""
                    },
                    label = { Text("New Password") },
                    visualTransformation = if (newPasswordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = if (newPasswordVisible)
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (newPasswordVisible)
                                    "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = newPasswordError.isNotEmpty(),
                    supportingText = if (newPasswordError.isNotEmpty()) {
                        { Text(newPasswordError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = ""
                    },
                    label = { Text("Confirm New Password") },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible)
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible)
                                    "Hide password" else "Show password"
                            )
                        }
                    },
                    isError = confirmPasswordError.isNotEmpty(),
                    supportingText = if (confirmPasswordError.isNotEmpty()) {
                        { Text(confirmPasswordError, color = MaterialTheme.colorScheme.error) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFE0E0E0)
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    var hasError = false

                    if (currentPassword.isEmpty()) {
                        currentPasswordError = "Current password is required"
                        hasError = true
                    }

                    if (newPassword.isEmpty()) {
                        newPasswordError = "New password is required"
                        hasError = true
                    } else if (!isPasswordValid(newPassword)) {
                        newPasswordError = "Password doesn't meet requirements"
                        hasError = true
                    }

                    if (confirmPassword.isEmpty()) {
                        confirmPasswordError = "Please confirm your password"
                        hasError = true
                    } else if (newPassword != confirmPassword) {
                        confirmPasswordError = "Passwords do not match"
                        hasError = true
                    }

                    if (!hasError) {
                        isLoading = true
                        changePassword(
                            auth = auth,
                            currentPassword = currentPassword,
                            newPassword = newPassword,
                            onSuccess = {
                                isLoading = false
                                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onError = { errorMessage ->
                                isLoading = false
                                currentPasswordError = errorMessage
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    if (isLoading) "Changing Password..." else "Change Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PasswordSection(title: String, content: @Composable ColumnScope.() -> Unit) {
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
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

fun isPasswordValid(password: String): Boolean {
    val hasMinLength = password.length >= 8
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }

    return hasMinLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
}

// Firebase password change function
fun changePassword(
    auth: FirebaseAuth,
    currentPassword: String,
    newPassword: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val user = auth.currentUser

    if (user?.email == null) {
        onError("No authenticated user found")
        return
    }

    // To Re-authenticate before changing
    val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)

    user.reauthenticate(credential)
        .addOnCompleteListener { reAuthTask ->
            if (reAuthTask.isSuccessful) {
                // Update
                user.updatePassword(newPassword)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            onSuccess()
                        } else {
                            onError(updateTask.exception?.message ?: "Failed to update password")
                        }
                    }
            } else {
                onError("Current password is incorrect")
            }
        }
}
