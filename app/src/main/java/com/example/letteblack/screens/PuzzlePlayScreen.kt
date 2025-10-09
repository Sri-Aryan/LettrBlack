package com.example.letteblack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.letteblack.R

@Composable
fun PuzzlePlayScreen(
    puzzleTitle: String,
    puzzleImage: Int,
    puzzleDescription: String,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardHeight = 220.dp

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top half: image background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
                painter = painterResource(puzzleImage),
                contentDescription = puzzleTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // subtle gradient so overlapping card stands out
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.75f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.40f)
                            )
                        )
                    )
            )
        }

        // Bottom half
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFF8F9FA))
                .padding(horizontal = 16.dp)
        ) {
            //---------card------------------------------//
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .height(cardHeight)
                    .align(Alignment.TopCenter)
                    .offset(y = (-cardHeight / 2)),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Image(
                    painter = painterResource(puzzleImage),
                    contentDescription = puzzleTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            //--------------------------button-----------------------------//
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = cardHeight / 2 + 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = puzzleTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(
                        text = "Play",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = puzzleDescription,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun PuzzlePlayPreview() {
    PuzzlePlayScreen(
        puzzleTitle = "Sample Puzzle",
        puzzleImage = R.drawable.ic_launcher_background,
        puzzleDescription = "This is a sample puzzle description.",
        onPlayClick = {}
    )
}
