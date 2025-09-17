package com.example.letteblack.components.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letteblack.model.CategoryModel


// ----------------- CATEGORY COMPONENT ----------------- //
@Composable
fun CategoryComponent(
    categoryModel: CategoryModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = categoryModel.categoryName,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )

        Spacer(Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            categoryModel.subCourses.forEach { subCourse ->
                SubCourseCard(subCourseModel = subCourse)
            }
        }
    }
}
