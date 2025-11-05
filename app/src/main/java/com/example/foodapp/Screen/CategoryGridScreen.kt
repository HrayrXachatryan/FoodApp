package com.example.foodapp.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodapp.Retrofit.CategoryViewModel
import coil.compose.AsyncImage



@Composable
fun CategoryGridScreen(
//    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    val rows = (categories.size + 2) / 3 // 3 колонки
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .height(rows * 120.dp) // автоматически считаем высоту
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false // 🔥 отключаем внутренний скролл
    ){
        items(categories) { category ->
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        viewModel.selectedCategoryId.value = category.Id
                    }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = category.ImagePath,
                    contentDescription = category.Name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = category.Name, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}



