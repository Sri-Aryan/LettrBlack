package com.example.letteblack.screens.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController, viewModel: OnBoardingViewModel) {

    val items = viewModel.items
    val pagerState = rememberPagerState(
        pageCount = { viewModel.items.size },
        initialPage = 0
    )
    val targetColor = items[pagerState.currentPage].color
    val animatedColor by animateColorAsState(targetColor)
    Box(modifier = Modifier.background(animatedColor)) {
        HorizontalPager(state = pagerState) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                UpScreen(pagerState, viewModel = viewModel)
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            DownScreen(navController, viewModel, pagerState)
        }
    }

}



