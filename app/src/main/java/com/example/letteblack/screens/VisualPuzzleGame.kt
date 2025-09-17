package com.example.letteblack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Preview
@Composable
fun VisualPuzzleGame(modifier: Modifier = Modifier) {
    var score by remember { mutableStateOf(0) }
    var targetShape by remember { mutableStateOf("Circle") }

    val shapes = listOf("Circle", "Square", "Triangle")

    //-------------------------to pick a shaep------------------------------//
    fun newRound() {
        targetShape = shapes.random()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Score: $score", style = MaterialTheme.typography.headlineMedium)

        Text("Find the: $targetShape", style = MaterialTheme.typography.bodyLarge)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            //-------------------------------Circle---------------------------//
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Red, CircleShape)
                    .clickable {
                        if (targetShape == "Circle") score++ else score--
                        newRound()
                    }
            )

            //---------------------------Square-------------------------------//
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue, RoundedCornerShape(0.dp))
                    .clickable {
                        if (targetShape == "Square") score++ else score--
                        newRound()
                    }
            )

            //-------------------Traingle-----------------------//
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Green, triangleShape())
                    .clickable {
                        if (targetShape == "Triangle") score++ else score--
                        newRound()
                    }
            )
        }
    }
}
//------------------------------helper function to make triangle-----------------------------------//
fun triangleShape() = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}
