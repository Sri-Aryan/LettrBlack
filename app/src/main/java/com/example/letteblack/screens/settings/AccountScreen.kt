package com.example.letteblack.screens.settings

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.letteblack.R
import com.example.letteblack.data.Routes
import com.example.letteblack.viewmodel.UserViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavHostController,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.user.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReauthDialog by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firstore = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var isUploading by remember { mutableStateOf(false) }

    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            userViewModel.loadAvatarFromFirestore(uid)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val currentUser = user
            if (currentUser?.uid != null) {
                isUploading = true
                userViewModel.uploadAvatarToFirebase(
                    uid = currentUser.uid,
                    imageUri = it,
                    onSuccess = { downloadUrl ->
                        isUploading = false
                        Toast.makeText(context, "Avatar updated successfully!", Toast.LENGTH_SHORT).show()
                    },
                    onError = { e ->
                        isUploading = false
                        val errorMessage = when {
                            e.message?.contains("storage/unauthorized") == true ->
                                "Permission denied. Please check Firebase Storage rules."
                            e.message?.contains("storage/object-not-found") == true ->
                                "File not found. Please try again."
                            else -> "Upload failed: ${e.message}"
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e("AccountScreen", "Upload error", e)
                    }
                )
            } else {
                Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDDC5E7)
                )
            )
        },
        containerColor = Color(0xFFDDC5E7)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // top portion
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFFDDC5E7))
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = user?.name ?: "User",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = user?.email ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.Default.Person,
                            title = "Edit Password",
                            onClick = { navController.navigate("change_password")}
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Email,
                            title = "Payment Option",
                            onClick = {}
                        )
                        ProfileMenuItem(
                            icon = Icons.Default.Lock,
                            title = "Terms & Conditions",
                            onClick = { }
                        )

                        ProfileMenuItem(
                            icon = Icons.Default.Lock,
                            title = "Delete Account",
                            onClick = { showDeleteDialog = true }
                        )
                    }
                }
            }

            // overlapping card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user?.avatarUrl?.takeIf { it.isNotEmpty() } ?: R.drawable.lettrblack)
                            .crossfade(true)
                            .placeholder(R.drawable.lettrblack)
                            .error(R.drawable.lettrblack)
                            .build(),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape)
                            .clickable { launcher.launch("image/*") }
                    )

                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(120.dp),
                            color = Color(0xFF7E19A9)
                        )
                    }
                }
            }

            DeleteAccountDialog(
                showDialog = showDeleteDialog,
                onDismiss = { showDeleteDialog = false },
                onConfirmDelete = {
                    showDeleteDialog = false
                    showReauthDialog = true
                }
            )

            ReauthenticateDialog(
                showDialog = showReauthDialog,
                onDismiss = { showReauthDialog = false },
                onReauthenticate = { password ->
                    showReauthDialog = false
                    deleteUserAccount(
                        auth = auth,
                        firestore = firstore,
                        password = password,
                        onSuccess = {
                            Toast.makeText(context, "Account Deleted Successfully", Toast.LENGTH_LONG).show()
                            navController.navigate(Routes.Login.toString()) { popUpTo(0) { inclusive = true } }
                        },
                        onError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = Color.Black,
            modifier = Modifier
                .size(20.dp)
                .then(Modifier.offset(x = 0.dp))
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 4.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}


@Composable
fun AccountInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                value,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun DeleteAccountDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Delete Account")
            },
            text = {
                Text(
                    "Are you sure you want to delete your account? " +
                            "This action cannot be undone and all your data will be permanently deleted."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmDelete
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color(0xFF000000))
                }
            }
        )
    }
}

@Composable
fun ReauthenticateDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onReauthenticate: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Verify Identity") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Please enter your password to confirm account deletion")

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { onReauthenticate(password) },
                    enabled = password.isNotEmpty()
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun deleteUserAccount(
    auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val user = auth.currentUser

    if (user?.email == null) {
        onError("No authenticated user found")
        return
    }

    // Re-authenticate as per firebase
    val credential = EmailAuthProvider.getCredential(user.email!!, password)

    user.reauthenticate(credential)
        .addOnSuccessListener {
            deleteUserDataFromFirestore(firestore, user.uid) { firestoreSuccess ->  // For deletion of user data
                if (firestoreSuccess) {
                    user.delete()           // Deleting Auth
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onError("Failed to delete account: ${exception.message}")
                        }
                } else {
                    onError("Failed to delete user data")
                }
            }
        }
        .addOnFailureListener { exception ->
            when {
                exception.message?.contains("password") == true -> {
                    onError("Incorrect password")
                }
                else -> {
                    onError("Authentication failed: ${exception.message}")
                }
            }
        }
}

fun deleteUserDataFromFirestore(
    firestore: FirebaseFirestore,
    userId: String,
    onComplete: (Boolean) -> Unit
) {
    val batch = firestore.batch()

    val userRef = firestore.collection("users").document(userId)
    batch.delete(userRef)

    // Deleting for subcollections
    firestore.collection("users").document(userId)
        .collection("progress")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                batch.delete(document.reference)
            }
            // Commiting deletions
            batch.commit()
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        }
        .addOnFailureListener {
            onComplete(false)
        }
}
