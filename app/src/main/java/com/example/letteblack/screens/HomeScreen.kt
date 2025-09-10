package com.example.letteblack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.letteblack.AuthViewModel
import com.example.letteblack.R
import com.example.letteblack.UserState
import com.example.letteblack.Utils
import com.example.letteblack.components.CategoryComponent
import com.example.letteblack.components.MockData
import com.example.letteblack.data.Routes
import com.example.letteblack.data.UserDetails
import com.example.letteblack.screens.groups.GroupListScreen
import com.example.letteblack.screens.groups.JoinGroupScreen
import com.example.letteblack.screens.notes.*
import com.example.letteblack.screens.tasks.*
import com.example.letteblack.viewmodel.NotesViewModel
import com.example.letteblack.viewmodel.TaskViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.letteblack.streak.StreakManager
import kotlinx.coroutines.launch

// ----------------- HOME SCREEN ----------------- //
@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    LaunchedEffect(authViewModel.userState.value) {
        when (authViewModel.userState.value) {
            is UserState.Unauthenticated -> navController.navigate(Routes.Login.toString())
            else -> {}
        }
    }

    var user by remember { mutableStateOf(UserDetails()) }

    // Load user details
    LaunchedEffect(Unit) {
        Utils.uid()?.let {
            Firebase.firestore.collection("users")
                .document(it).get().addOnCompleteListener { listener ->
                    if (listener.isSuccessful) {
                        user = listener.result.toObject(UserDetails()::class.java) as UserDetails
                    }
                }
        }
    }

    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(innerNavController) }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {

            // ---------- HOME ---------- //
            composable("home") { HomeContent(user, authViewModel) }

            // ---------- GROUPS ---------- //
            composable("courses") {
                GroupListScreen(
                    userId = user.uid,
                    onGroupClick = { groupId ->
                        innerNavController.navigate("group/$groupId")
                    }
                )
            }
            composable("group/{groupId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                JoinGroupScreen(
                    groupId = groupId,
                    userId = user.uid,
                    navController = innerNavController,
                    onAddNoteClick = { gId, _ -> innerNavController.navigate("group/$gId/addNote") },
                    onAddTaskClick = { gId, _ -> innerNavController.navigate("group/$gId/addTask") }
                )
            }

            // ---------- NOTES ---------- //
            composable("group/{groupId}/addNote") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                AddNoteScreen(groupId, user.uid) { innerNavController.popBackStack() }
            }
            composable("group/{groupId}/notes") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val viewModel: NotesViewModel = hiltViewModel()
                NotesSection(groupId, viewModel) { note ->
                    navController.navigate("group/$groupId/noteDetail/${note.noteId}")
                }
            }
            composable("group/{groupId}/noteDetail/{noteId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val noteId = backStackEntry.arguments?.getString("noteId")!!
                val viewModel: NotesViewModel = hiltViewModel()
                val note by viewModel.getNoteById(noteId).collectAsState(initial = null)
                note?.let {
                    NoteDetailScreen(it, viewModel,
                        onEdit = { noteEntity -> innerNavController.navigate("group/$groupId/noteEdit/${noteEntity.noteId}") },
                        onDeleted = { innerNavController.popBackStack() }
                    )
                }
            }
            composable("group/{groupId}/noteEdit/{noteId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val noteId = backStackEntry.arguments?.getString("noteId")!!
                val viewModel: NotesViewModel = hiltViewModel()
                val note by viewModel.getNoteById(noteId).collectAsState(initial = null)
                note?.let {
                    UpdateNoteScreen(it, viewModel) { innerNavController.popBackStack() }
                }
            }

            // ---------- TASKS ---------- //
            composable("group/{groupId}/addTask") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                val taskViewModel: TaskViewModel = hiltViewModel()
                AddTaskScreen(
                    groupId = groupId,
                    assignerId = user.uid,
                    assigneeId = "someUserId",
                    viewModel = taskViewModel
                ) { innerNavController.popBackStack() }
            }
            composable("group/{groupId}/taskDetail/{taskId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val taskId = backStackEntry.arguments?.getString("taskId")!!
                val viewModel: TaskViewModel = hiltViewModel()
                val task by viewModel.getTaskById(taskId).collectAsState(initial = null)
                task?.let {
                    TaskDetailScreen(it, viewModel,
                        onEdit = { taskEntity -> innerNavController.navigate("group/$groupId/taskEdit/${taskEntity.taskId}") },
                        onDeleted = { innerNavController.popBackStack() }
                    )
                }
            }
            composable("group/{groupId}/taskEdit/{taskId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val taskId = backStackEntry.arguments?.getString("taskId")!!
                val viewModel: TaskViewModel = hiltViewModel()
                val task by viewModel.getTaskById(taskId).collectAsState(initial = null)
                task?.let {
                    UpdateTaskScreen(it, viewModel,
                        onTaskUpdated = { innerNavController.popBackStack() },
                        onCancel = { innerNavController.popBackStack() }
                    )
                }
            }

            // ---------- OTHER ---------- //
            composable("puzzles") { CenterText("Puzzles") }
            composable("you") { ProfileScreen(navController) }
        }
    }
}

// ----------------- HOME CONTENT ----------------- //
@Composable
fun HomeContent(user: UserDetails, authViewModel: AuthViewModel) {
    val categoryList = remember { MockData.mockCategories() }

    // Context for SharedPreferences
    val context = LocalContext.current

    // Streak state
    var streak by remember { mutableStateOf(0) }
    var showMilestone by remember { mutableStateOf(false) }

    // Load streak on first render
    LaunchedEffect(Unit) {
        val result = StreakManager.recordStudySession(context)
        streak = result.streak
        if (result.streakMilestone) {
            showMilestone = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // ---------- HEADER ---------- //
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lettrblack),
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                )
            }
        }

        // ---------- TITLE ---------- //
        item {
            Text(
                "Discover, Learn, and Grow",
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
        }

        // ---------- DAILY STREAK BANNER ---------- //
        item { StreakBanner(streak) }

        // ---------- MILESTONE POPUP ---------- //
        if (showMilestone) {
            item {
                MilestoneCard(streak) { showMilestone = false }
            }
        }

        // ---------- CATEGORIES ---------- //
        items(categoryList) { category ->
            CategoryComponent(categoryModel = category)
        }
    }
}

// ----------------- STREAK BANNER ----------------- //
@Composable
fun StreakBanner(streak: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE082)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = "🔥 $streak Day Streak",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD84315)
            )
        }
    }
}

// ----------------- MILESTONE CARD ----------------- //
@Composable
fun MilestoneCard(streak: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Awesome!")
            }
        },
        title = { Text("🎉 Milestone Reached!") },
        text = { Text("Congratulations! You've hit a $streak-day streak!") }
    )
}

// ----------------- BOTTOM NAV ----------------- //
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Groups", "courses", Icons.Default.Groups3),
        BottomNavItem("Puzzles", "puzzles", Icons.Default.Build),
        BottomNavItem("You", "you", Icons.Default.Person)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun CenterText(txt: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(txt, fontSize = 22.sp, fontWeight = FontWeight.Medium)
    }
}
