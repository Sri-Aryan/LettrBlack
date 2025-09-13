package com.example.letteblack.components.puzzle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.letteblack.R
import com.example.letteblack.model.PuzzleModel

@Composable
fun TopPuzzleComponent(puzzle: PuzzleModel,onClick: () -> Unit, modifier: Modifier = Modifier) {


    Box(
        modifier = modifier
            .width(140.dp)
            .height(170.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable(onClick =onClick )

    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()

        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.7f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        Box(modifier = Modifier
            .matchParentSize()
            .padding(5.dp),
            contentAlignment = Alignment.BottomCenter){
            Text(puzzle.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }


}