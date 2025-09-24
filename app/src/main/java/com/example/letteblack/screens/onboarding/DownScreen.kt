package com.example.letteblack.screens.onboarding

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.letteblack.R
import com.example.letteblack.data.Routes
import com.example.letteblack.screens.onboarding.data.ScreenData
import com.example.letteblack.screens.onboarding.data.UserPreference
import com.example.letteblack.ui.theme.BottomCardShape
import kotlinx.coroutines.launch

@Composable
fun DownScreen(
    navController: NavController, viewModel: OnBoardingViewModel, pagerState: PagerState
) {
    val items = viewModel.items
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(
            16.dp

        ),
        shape = BottomCardShape.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                PagerIndicator(items = items, currentPage = pagerState.currentPage)
                Text(
                    text = items[pagerState.currentPage].motiveText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            }
            TextButton(
                onClick = {
                    navController.navigate(Routes.Home.toString())
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("Skip")
            }

            val coroutineScope = rememberCoroutineScope()

            val width = animateDpAsState(if (pagerState.currentPage == 2) 120.dp else 65.dp)
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage != 2) {

                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            navController.navigate(Routes.Home.toString())
                            val userPreference = UserPreference(
                                subjectPreference = viewModel.selectedSubjects,
                                goalPreference = viewModel.selectedGoals,
                                studyTimePreference = viewModel.selectedStudyTime
                            )
                            Log.d("MYTAG", "DownScreen: ${userPreference.copy()}")

                        }
                    }
                },
                shape = RoundedCornerShape(50.dp),
                containerColor = items[pagerState.currentPage].color,
                contentColor = Color.White,
                modifier = Modifier
                    .size(width = width.value, height = 65.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp),
            ) {
                if (pagerState.currentPage == 2) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = ""
                    )

                } else {
                    Icon(
                        painter = painterResource(R.drawable.next), contentDescription = ""
                    )
                }
            }


        }

    }
}

@Composable
fun PagerIndicator(items: ArrayList<ScreenData>, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(top = 20.dp)
    ) {

        repeat(items.size) {
            Indicator(
                isSelected = it == currentPage, color = items[currentPage].color
            )
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean, color: Color) {
    val width = animateDpAsState(targetValue = if (isSelected) 40.dp else 10.dp)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(if (isSelected) color else Color.Gray.copy(alpha = 0.5f))
    )
}
