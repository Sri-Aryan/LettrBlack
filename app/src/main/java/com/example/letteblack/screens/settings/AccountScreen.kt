package com.example.letteblack.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.letteblack.R
import com.example.letteblack.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onDeleteAccount: () -> Unit = {},
    navController: NavHostController,
) {
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.user.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            userViewModel.uploadAvatarToFirebase(
                uid = user?.uid ?: "",
                imageUri = it,
                onSuccess = { downloadUrl ->
                    userViewModel.saveAvatarUrlToFirestore(user?.uid ?: "", downloadUrl)
                    userViewModel.setAvatar(downloadUrl)
                },
                onError = { e -> e.printStackTrace() }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account") },
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
            // Avatar
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = user?.avatarUri ?: R.drawable.lettrblack
                    ),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                TextButton(onClick = { launcher.launch("image/*") }) {
                    Text("Change Avatar", fontSize = 14.sp)
                }
            }

            SettingsSection("Personal Info") {
                AccountInfoItem(
                    icon = Icons.Default.Person,
                    title = "${user?.name}",
                    value = "Name",
                    onClick = {}
                )
                AccountInfoItem(
                    icon = Icons.Default.AccountCircle,
                    title = "${user?.uid}",
                    value = "UserID",
                    onClick = {}
                )
            }

            SettingsSection("Account Security") {
                AccountInfoItem(
                    icon = Icons.Default.Email,
                    title = "${user?.email}",
                    value = "Email",
                    onClick = {}
                )
                AccountInfoItem(
                    icon = Icons.Default.Lock,
                    title = "Password",
                    value = "••••••••",
                    onClick = {navController.navigate("change_password")}
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onDeleteAccount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 42.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete Account", color = MaterialTheme.colorScheme.onError)
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
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(value,style = MaterialTheme.typography.bodySmall)
        }
    }
}
