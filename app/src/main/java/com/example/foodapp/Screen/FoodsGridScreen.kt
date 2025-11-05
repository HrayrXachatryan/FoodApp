package com.example.foodapp.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.Retrofit.CategoryViewModel
import com.example.foodapp.Retrofit.FoodsViewModel
import com.example.foodapp.R



@Composable
fun FoodsGridScreen(
    navController: NavController,
    viewModel: FoodsViewModel = viewModel()
) {
    val foods by viewModel.foods.collectAsState()
    val categoryViewModel: CategoryViewModel = viewModel()
    val foodsViewModel: FoodsViewModel = viewModel()

    val selectedCategoryId by categoryViewModel.selectedCategoryId

    LaunchedEffect(selectedCategoryId) {
        foodsViewModel.fetchFoods(selectedCategoryId)
    }
    if (foods.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val rows = (foods.size + 1) / 2 // 2 колонки
        Text(
            text = "Foods for you",
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp
                ),
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .height(rows * 230.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            userScrollEnabled = false
        ) {
            items(foods) { food ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {navController.navigate("details/${food.Id}") },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = food.ImagePath,
                            contentDescription = food.Title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = food.Title,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Star",
                                tint = Color.Yellow,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Text("${food.Star}")
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "$${food.Price}", style = MaterialTheme.typography.bodyMedium)
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.time),
                                    contentDescription = "time",
                                    modifier = Modifier
                                        .size(12.dp)
                                        .padding(top = 3.dp)
                                )
                                Text(text = "${food.TimeValue} min")
                            }
                        }
                    }
                }
            }
        }
    }
}



