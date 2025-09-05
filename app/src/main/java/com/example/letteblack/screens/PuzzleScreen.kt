package com.example.letteblack.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letteblack.components.puzzle.CategoryPuzzleComponent
import com.example.letteblack.components.puzzle.TopPuzzleComponent
import com.example.letteblack.ui.theme.LetteBlackTheme
import com.example.letteblack.util.MockData

@Composable
fun PuzzleScreen(
    onClick: () -> Unit,
    onPuzzleClick:()->Unit,
    modifier: Modifier = Modifier
) {
    val puzzleCategoryList = remember { MockData.getPuzzleCategories() }
    val topPuzzleList = remember { MockData.getTopPuzzles() }
    LazyColumn(modifier = modifier
        .fillMaxSize()) {

        //-------HEADER---------------//
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Text(
                    "Quizzes & Puzzles",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

            }
        }

        item {
            Text(
                text = "Top Puzzles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }


        //---------------------Top Puzzle List----------------------//
        item{
            LazyRow (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp,vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                items(topPuzzleList){puzzle->
                    TopPuzzleComponent(puzzle, onClick = onPuzzleClick)
                }
            }
        }

        //-------------Category Title-----------------------//
        item {
            Text(
                text = "Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(Modifier.height(20.dp))
        }

        //--------------------Category puzzles-------------------//
        items(puzzleCategoryList) { category ->
            CategoryPuzzleComponent(category,
                onClick = onClick,
                modifier=Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(10.dp))
        }



    }

}


@Preview(showBackground = true)
@Composable
private fun PuzzlePreview() {
    LetteBlackTheme {
        PuzzleScreen(
            onClick = {},
            onPuzzleClick = {}
        )
    }


}