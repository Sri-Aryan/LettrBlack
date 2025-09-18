package com.example.letteblack.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.letteblack.ui.theme.Poppins
import com.example.letteblack.ui.theme.ReemKufi
@OptIn(ExperimentalFoundationApi::class)
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
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
                    color = Color.DarkGray
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
                                modifier = Modifier.height(height = 22.dp),
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

        1 -> FlowRow {

            repeat(goals.size) { index ->
                val goal = goals[index]
                var isSelected by remember { mutableStateOf(goal.selected) }
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(width = 100.dp, height = 100.dp)
                        .clickable {
                            isSelected = !isSelected
                            goal.selected = isSelected
                        },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF1565C0) else Color(0xFFE3F2FD)
                    )

                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(goal.icon),

                            contentDescription = goal.name,
                            tint = if (isSelected) Color.White else Color(0xFF1565C0)
                        )
                        Text(
                            text = goal.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
        }

        2 -> {

        }
    }


}



