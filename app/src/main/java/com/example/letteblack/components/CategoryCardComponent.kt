package com.example.letteblack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.letteblack.model.CategoryModel
import com.example.letteblack.model.SubCourseModel
import com.example.letteblack.R

// ----------------- CATEGORY LIST (SCREEN) ----------------- //
@Composable
fun CategoryCardComponent(
    categories: List<CategoryModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(categories) { category ->
            CategoryComponent(categoryModel = category)
        }
    }
}

// ----------------- PREVIEW ----------------- //
@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    val categories = remember { mockCategories() }
    CategoryCardComponent(categories = categories, modifier = Modifier.statusBarsPadding())
}

// ----------------- MOCK DATA ----------------- //
fun mockCategories(): List<CategoryModel> = listOf(
    CategoryModel(
        categoryName = "Data Analysis",
        subCourses = listOf(
            SubCourseModel("Exploring Data Visually", R.drawable.ic_launcher_background),
            SubCourseModel("Probability in Data", R.drawable.ic_launcher_background),
            SubCourseModel("Clustering and Classification", R.drawable.ic_launcher_background),
            SubCourseModel("Regression", R.drawable.ic_launcher_background),
        )
    ),
    CategoryModel(
        categoryName = "Computer Science",
        subCourses = listOf(
            SubCourseModel("Variables", R.drawable.ic_launcher_background),
            SubCourseModel("Functions", R.drawable.ic_launcher_background),
            SubCourseModel("Algorithms", R.drawable.ic_launcher_background),
            SubCourseModel("Artificial Intelligence", R.drawable.ic_launcher_background),
        )
    ),
    CategoryModel(
        categoryName = "Mobile Development",
        subCourses = listOf(
            SubCourseModel("Kotlin Basics", R.drawable.ic_launcher_background),
            SubCourseModel("Jetpack Compose", R.drawable.ic_launcher_background),
            SubCourseModel("Android Architecture Components", R.drawable.ic_launcher_background),
            SubCourseModel("Firebase Integration", R.drawable.ic_launcher_background),
        )
    )
)