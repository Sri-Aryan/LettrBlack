package com.example.letteblack.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.scale
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
import com.example.letteblack.AuthViewModel
import com.example.letteblack.R
import com.example.letteblack.UserState
import com.example.letteblack.Utils
import com.example.letteblack.data.Routes
import com.example.letteblack.data.UserDetails
import com.example.letteblack.screens.groups.GroupListScreen
import com.example.letteblack.screens.groups.JoinGroupScreen
import com.example.letteblack.screens.notes.AddNoteScreen
import com.example.letteblack.screens.notes.NoteDetailScreen
import com.example.letteblack.screens.notes.NotesSection
import com.example.letteblack.screens.notes.UpdateNoteScreen
import com.example.letteblack.screens.tasks.AddTaskScreen
import com.example.letteblack.screens.tasks.TaskDetailScreen
import com.example.letteblack.screens.tasks.UpdateTaskScreen
import com.example.letteblack.viewmodel.NotesViewModel
import com.example.letteblack.viewmodel.TaskViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable

fun HomeScreen(navController: NavHostController, modifier: Modifier, authViewModel: AuthViewModel) {
    LaunchedEffect(authViewModel.userState.value) {
        when (authViewModel.userState.value) {

            is UserState.Unauthenticated -> navController.navigate(Routes.Login.toString())
            else -> null
        }
    }

    var user: UserDetails by remember { mutableStateOf(UserDetails()) }
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

            composable("home") { HomeContent(user, authViewModel) }
            composable("courses") {
                GroupListScreen(
                    userId = user.uid, // pass logged-in userId
                    onGroupClick = { groupId ->
                        innerNavController.navigate("group/$groupId")
                    }
                )
            }
            // ---------- Group Main ----------
            composable("group/{groupId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                JoinGroupScreen(
                    groupId = groupId,
                    userId = user.uid,
                    navController = innerNavController,
                    onAddNoteClick = { gId, uId ->
                        innerNavController.navigate("group/$gId/addNote")
                    },
                    onAddTaskClick = { gId, uId ->
                        innerNavController.navigate("group/$gId/addTask")
                    }
                )
            }

            // ---------- Add Note ----------
            composable("group/{groupId}/addNote") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                AddNoteScreen(
                    groupId = groupId,
                    userId = user.uid,
                    onNoteSaved = {
                        innerNavController.popBackStack()
                    })
            }

            // ---------- Notes List ----------
            composable("group/{groupId}/notes") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val viewModel: NotesViewModel = hiltViewModel()

                NotesSection(
                    groupId = groupId,
                    viewModel = viewModel,
                    onNoteClick = { note ->
                        navController.navigate("group/$groupId/noteDetail/${note.noteId}")
                    }
                )
            }

            // ---------- Note Detail ----------
            composable("group/{groupId}/noteDetail/{noteId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val noteId = backStackEntry.arguments?.getString("noteId")!!
                val viewModel: NotesViewModel = hiltViewModel()

                val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

                note?.let {
                    NoteDetailScreen(
                        note = it,
                        viewModel = viewModel,
                        onEdit = { noteEntity ->
                            innerNavController.navigate("group/$groupId/noteEdit/${noteEntity.noteId}")
                        },
                        onDeleted = { innerNavController.popBackStack() }
                    )
                }
            }

            // ---------- Update Note ----------
            composable("group/{groupId}/noteEdit/{noteId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val noteId = backStackEntry.arguments?.getString("noteId")!!
                val viewModel: NotesViewModel = hiltViewModel()

                val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

                note?.let {
                    UpdateNoteScreen(
                        note = it,
                        viewModel = viewModel,
                        onNoteUpdated = { innerNavController.popBackStack() }
                    )
                }
            }


            // ---------- Add Task ----------
            composable("group/{groupId}/addTask") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
                val taskViewModel: TaskViewModel = hiltViewModel()

                AddTaskScreen(
                    groupId = groupId,
                    assignerId = user.uid,
                    assigneeId = "someUserId",
                    viewModel = taskViewModel,
                    onTaskSaved = { innerNavController.popBackStack() }
                )
            }

            // ---------- Task Detail ----------
            composable("group/{groupId}/taskDetail/{taskId}") { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")!!
                val taskId = backStackEntry.arguments?.getString("taskId")!!
                val viewModel: TaskViewModel = hiltViewModel()

                val task by viewModel.getTaskById(taskId).collectAsState(initial = null)

                task?.let {
                    TaskDetailScreen(
                        task = it,
                        viewModel = viewModel,
                        onEdit = { taskEntity ->
                            innerNavController.navigate("group/$groupId/taskEdit/${taskEntity.taskId}")
                        },
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
                    UpdateTaskScreen(
                        task = it,
                        groupId = groupId,
                        viewModel = viewModel,
                        onTaskUpdated = { innerNavController.popBackStack() },
                        onCancel = { innerNavController.popBackStack() }
                    )
                }
            }

            composable("leaderboard") {
                LeaderboardScreen(groupId = "your_group_id_here")
            }

            composable("puzzles") { CenterText("Puzzles") }
            composable("you") { ProfileScreen(navController) }
        }
    }
}

@Composable
fun HomeContent(user: UserDetails, authViewModel: AuthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp, top = 10.dp),
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

            TextButton(onClick = { authViewModel.signOut() }) {
                Text("Logout", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(35.dp))
        Text(
            "Discover, Learn, and Grow",
            fontSize = 22.sp,
            fontFamily = FontFamily.SansSerif,

            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(70.dp))

        val sections = listOf("Maths", "Science", "History & Civics", "Others")
        val colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.errorContainer
        )

        sections.forEachIndexed { index, section ->
            AnimatedCard(title = section, containerColor = colors[index % colors.size])
            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}

@Composable
fun AnimatedCard(title: String, containerColor: androidx.compose.ui.graphics.Color) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 1.05f else 1f, label = "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .scale(scale)
            .clickable { pressed = !pressed },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Courses", "courses", Icons.Default.ShoppingCart),
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
