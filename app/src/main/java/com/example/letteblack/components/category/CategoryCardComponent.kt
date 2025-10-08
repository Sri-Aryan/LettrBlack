package com.example.letteblack.components.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.letteblack.model.CategoryModel
import com.example.letteblack.util.MockData

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
    val categories = remember { MockData.mockCategories() }
    CategoryCardComponent(categories = categories, modifier = Modifier.statusBarsPadding())
}

// ----------------- MOCK DATA ----------------- //



