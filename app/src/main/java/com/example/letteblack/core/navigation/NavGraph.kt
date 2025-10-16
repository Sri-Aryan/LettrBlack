package com.example.letteblack.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.letteblack.data.local.entities.UserEntity
import com.example.letteblack.data.remote.Routes
import com.example.letteblack.data.remote.UserDetails
import com.example.letteblack.presentation.screen.groups.*
import com.example.letteblack.presentation.screen.home.HomeContent
import com.example.letteblack.presentation.screen.home.components.CenterText
import com.example.letteblack.presentation.screen.leaderboard.LeaderboardScreen
import com.example.letteblack.presentation.screen.notes.*
import com.example.letteblack.presentation.screen.profile.ProfileScreen
import com.example.letteblack.presentation.screen.puzzle.PuzzleScreen
import com.example.letteblack.presentation.screen.tasks.*
import com.example.letteblack.viewmodel.*

@Composable
fun NavGraph(
    innerNavController: NavHostController,
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    userInfo: UserEntity?,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = innerNavController,
        startDestination = "home",
        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
    ) {
        composable("home") {
            userInfo?.let { user ->
                HomeContent(
                    user = UserDetails(user.uid, user.name, user.email),
                    authViewModel = authViewModel
                )
            } ?: CenterText("Loading user...")
        }

        composable("courses") {
            userInfo?.let { user ->
                GroupListScreen(
                    userId = user.uid,
                    onGroupClick = { groupId ->
                        innerNavController.navigate("group/$groupId")
                    }
                )
            }
        }

        // ---------- Group Main ----------
        composable("group/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            userInfo?.let { user ->
                JoinGroupScreen(
                    groupId = groupId,
                    userId = user.uid,
                    navController = innerNavController,
                    onAddNoteClick = { gId, _ ->
                        innerNavController.navigate("group/$gId/addNote")
                    },
                    onAddTaskClick = { gId, _ ->
                        innerNavController.navigate("group/$gId/addTask")
                    }
                )
            }
        }

        // ---------- Add Note ----------
        composable("group/{groupId}/addNote") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            userInfo?.let { user ->
                AddNoteScreen(
                    groupId = groupId,
                    userId = user.uid,
                    onNoteSaved = { innerNavController.popBackStack() }
                )
            }
        }

        // ---------- Notes List ----------
        composable("group/{groupId}/notes") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")!!
            val viewModel: NotesViewModel = hiltViewModel()

            NotesSection(
                groupId = groupId,
                viewModel = viewModel,
                onNoteClick = { note ->
                    rootNavController.navigate("group/$groupId/noteDetail/${note.noteId}")
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
            userInfo?.let { user ->
                AddTaskScreen(
                    groupId = groupId,
                    assignerId = user.uid,
                    viewModel = taskViewModel,
                    onTaskSaved = { innerNavController.popBackStack() }
                )
            }
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
        composable("puzzles") {
            PuzzleScreen(
                onClick = {
                    rootNavController.navigate(Routes.PuzzleCategory.toString())
                },
                onPuzzleClick = { title, image, description ->

                    rootNavController.navigate("puzzlePlay/$title/$image/$description")
                }
            )
        }
        // ---------- Leaderboard ----------
        composable("leaderboard/{groupId}") { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId") ?: ""
            LeaderboardScreen(groupId = groupId)
        }

        composable("you") {
            userInfo?.let { user ->
                ProfileScreen(
                    navController = rootNavController, // Don't use innerNavController here
                    userId = user.uid,
                    userName = user.name
                )
            } ?: CenterText("Loading user...")
        }
    }
}