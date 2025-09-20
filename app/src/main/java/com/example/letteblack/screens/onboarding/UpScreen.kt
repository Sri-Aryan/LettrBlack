package com.example.letteblack.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.letteblack.R
import com.example.letteblack.ui.theme.Poppins
import com.example.letteblack.ui.theme.ReemKufi

@Composable
fun UpScreen(pagerState: PagerState, viewModel: OnBoardingViewModel) {
    val pageNumber = pagerState.currentPage
    val title = viewModel.items[pageNumber].mainTitle
    val animation = viewModel.items[pageNumber].animation
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(animation)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(
                    top = 8.dp, start = 8.dp,
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                alignment = Alignment.CenterEnd
            )
        }
        DisplayOptions(viewModel, pageNumber)

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisplayOptions(viewModel: OnBoardingViewModel, pageNumber: Int) {

    val subjects = viewModel.subjectsChip
    val goals = viewModel.goals
    var enabled by remember { mutableStateOf(true) }

    when (pageNumber) {
        0 -> repeat(subjects.size) { subjectIndex ->
            Column(modifier = Modifier.fillMaxWidth()) {
                val subjectChips = subjects[subjectIndex].subjects
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = subjects[subjectIndex].title,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Poppins,
                    color = Color.Black
                )

                if (enabled) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(subjectChips.size) { index ->
                            val chip = subjectChips[index]
                            InputChip(
                                modifier = Modifier.height(height = 24.dp),
                                selected = chip.isChecked,
                                onClick = {
                                    chip.isChecked = !chip.isChecked
                                },
                                label = {
                                    Text(
                                        chip.subjectTitle,
                                        fontFamily = ReemKufi,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Light
                                    )
                                },
                                avatar = {
                                    Icon(
                                        painter = painterResource(chip.icon),
                                        contentDescription = "Icon of Chip",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            )
                        }
                    }
                }

            }
        }

        1 -> LazyVerticalGrid(
            columns = GridCells.Fixed(3), // always 3 items per row
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp), // spacing between cards
            verticalArrangement = Arrangement.spacedBy(12.dp),   // spacing between rows
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // outer padding
        ) {
            items(goals.size) { index ->
                val goal = goals[index]
                var isSelected by remember { mutableStateOf(goal.selected) }

                Card(
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .clickable {
                            isSelected = !isSelected
                            goal.selected = isSelected
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 8.dp else 2.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1565C0) else Color(0xFFE3F2FD)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(goal.icon),
                            contentDescription = goal.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(4f)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit, // keeps aspect ratio, fills width
                            colorFilter = ColorFilter.tint( // instead of tint
                                if (isSelected) Color.White else Color(0xFF1565C0)
                            )
                        )

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),

                            text = goal.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        2 -> {
            Column(modifier = Modifier.padding(16.dp)) {

                // ---------------- Day Part ----------------
                Text(text = "Day part you study")
                val dayPartOptions = mapOf(
                    "Morning" to R.drawable.morning,
                    "Noon" to R.drawable.noon,
                    "Evening" to R.drawable.evening
                )
                var selectedDayPart by remember { mutableStateOf<String?>(null) }

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    dayPartOptions.forEach { item ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .padding(8.dp)
                                .clickable { selectedDayPart = item.key }, // ✅ CLICK HANDLER
                            elevation = CardDefaults.cardElevation(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (item.key == selectedDayPart) Color(0xFFD6EAF8) else Color.White
                            )
                        ) {
                            Column(
                                Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(item.value),
                                    contentDescription = item.key,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .padding(8.dp)
                                )
                                Text(
                                    text = item.key,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------------- Hours ----------------
                Text(text = "How many hours you study?")
                val hourOptions = listOf("30m", "1h", "2h", "2h+")
                var selectedHours by remember { mutableStateOf(hourOptions[1]) }

                Row {
                    hourOptions.forEach { text ->
                        Card(
                            modifier = Modifier
                                .size(width = 90.dp, height = 60.dp)
                                .padding(8.dp)
                                .clickable { selectedHours = text }, // ✅ CLICK HANDLER
                            elevation = CardDefaults.cardElevation(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (text == selectedHours) Color(0xFFD6EAF8) else Color.White
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = text, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ---------------- Session Style ----------------
                Text(text = "Which session style you like most?")
                val styleOptions = mapOf(
                    "Pomodoro" to R.drawable.pomodoro,
                    "Deep Work" to R.drawable.deep,
                    "Flow Mode" to R.drawable.flow
                )
                var selectedStyle by remember { mutableStateOf<String?>(null) }

                Row {
                    styleOptions.forEach { item ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(120.dp)
                                .padding(8.dp)
                                .clickable { selectedStyle = item.key }, // ✅ CLICK HANDLER
                            elevation = CardDefaults.cardElevation(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (item.key == selectedStyle) Color(0xFFD6EAF8) else Color.White
                            )
                        ) {
                            Column(
                                Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(item.value),
                                    contentDescription = item.key,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .padding(8.dp)
                                )
                                Text(
                                    text = item.key,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}

@Preview
@Composable
fun DisplayScreen() {
    DisplayOptions(viewModel = OnBoardingViewModel(), 0)
}

@Composable
fun DisplayRadioButton() {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(text = "Day part you study")
        val dayPartOptions = mapOf(
            "Morning" to R.drawable.morning,
            "Noon" to R.drawable.noon,
            "Evening" to R.drawable.evening
        )
        var selectedDayPart by remember { mutableStateOf<String?>(null) }

        Row {
            dayPartOptions.forEach { item ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(8.dp)
                        .clickable { selectedDayPart = item.key }, // ✅ CLICK HANDLER
                    elevation = CardDefaults.cardElevation(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.key == selectedDayPart) Color(0xFFD6EAF8) else Color.White
                    )
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(item.value),
                            contentDescription = item.key,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = item.key,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- Hours ----------------
        Text(text = "How many hours you study?")
        val hourOptions = listOf("30m", "1h", "2h", "2h+")
        var selectedHours by remember { mutableStateOf(hourOptions[1]) }

        Row {
            hourOptions.forEach { text ->
                Card(
                    modifier = Modifier
                        .size(width = 90.dp, height = 60.dp)
                        .padding(8.dp)
                        .clickable { selectedHours = text }, // ✅ CLICK HANDLER
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (text == selectedHours) Color(0xFFD6EAF8) else Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = text, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- Session Style ----------------
        Text(text = "Which session style you like most?")
        val styleOptions = mapOf(
            "Pomodoro" to R.drawable.pomodoro,
            "Deep Work" to R.drawable.deep,
            "Flow Mode" to R.drawable.flow
        )
        var selectedStyle by remember { mutableStateOf<String?>(null) }

        Row {
            styleOptions.forEach { item ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .padding(8.dp)
                        .clickable { selectedStyle = item.key }, // ✅ CLICK HANDLER
                    elevation = CardDefaults.cardElevation(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.key == selectedStyle) Color(0xFFD6EAF8) else Color.White
                    )
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(item.value),
                            contentDescription = item.key,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = item.key,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}



