package com.example.letteblack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.letteblack.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    groupId: String,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val members by viewModel.observeLeaderboard(groupId).collectAsState(initial = emptyList())

    LaunchedEffect(members) {
        println("Leaderboard members: $members")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Leaderboard") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (members.isEmpty()) {
                Text("No members yet")
            } else {
                // --- Top 3 Podium ---
                val sortedMembers = members.sortedByDescending { it.points }
                val top3 = sortedMembers.take(3)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (top3.size > 1) {
                        PodiumBox(position = 2, name = top3[1].userName, points = top3[1].points)
                    }
                    if (top3.isNotEmpty()) {
                        PodiumBox(
                            position = 1,
                            name = top3[0].userName,
                            points = top3[0].points,
                            isWinner = true
                        )
                    }
                    if (top3.size > 2) {
                        PodiumBox(position = 3, name = top3[2].userName, points = top3[2].points)
                    }
                }

                // --- Ranking List ---
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(sortedMembers) { index, member ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${index + 1}", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    member.userName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "${member.points} pts",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumBox(position: Int, name: String, points: Int, isWinner: Boolean = false) {
    val height = when (position) {
        1 -> 120.dp
        2 -> 90.dp
        3 -> 80.dp
        else -> 70.dp
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("$points pts", fontSize = 12.sp, color = Color.Gray)
        Box(
            modifier = Modifier
                .width(70.dp)
                .height(height)
                .background(
                    when (position) {
                        1 -> Color(0xFFFFD700) // Gold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> Color.LightGray
                    },
                    shape = MaterialTheme.shapes.small
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$position",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}