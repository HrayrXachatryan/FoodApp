package com.example.foodapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.Retrofit.FoodsViewModel

@Composable
fun DetailsScreen(
    navController: NavController,
    foodId: Int,
    viewModel: FoodsViewModel
) {
    val food by viewModel.foodDetails.collectAsState()
    val foodCount by viewModel.foodsCount.collectAsState()
    val recommendedFoods by viewModel.recommendedImagePath.collectAsState()
    val favorites by viewModel.favoriteFoods.collectAsState()
    val isFavorite = favorites.any { it.Id == food?.Id }

    LaunchedEffect(foodId) {
        viewModel.fetchFoodDetails(foodId)
    }
    LaunchedEffect(Unit) {
        viewModel.fetchRecommendedFoods()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = food?.ImagePath,
                contentDescription = food?.Title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Текст с возможностью переноса на 2 строки
                    Text(
                        text = food?.Title ?: "",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Блок с +/- кнопками
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(50))
                            .height(28.dp) // чуть выше
                            .padding(horizontal = 8.dp), // чуть больше отступ
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Кнопка "-"
                        IconButton(
                            onClick = { viewModel.decrementCount() },
                            modifier = Modifier
                                .size(24.dp) // чуть больше
                                .background(Color.LightGray, shape = CircleShape)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "-",
                                modifier = Modifier.size(16.dp), // чуть больше
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp)) // чуть больше расстояние до числа

                        // Число
                        Text(
                            text = foodCount.toString(),
                            fontSize = 16.sp // чуть больше
                        )

                        Spacer(modifier = Modifier.width(12.dp)) // чуть больше расстояние до "+"

                        // Кнопка "+"
                        IconButton(
                            onClick = { viewModel.incrementCount() },
                            modifier = Modifier
                                .size(24.dp) // чуть больше
                                .background(Color(0xFF4CAF50), shape = CircleShape)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "+",
                                modifier = Modifier.size(16.dp), // чуть больше
                                tint = Color.White
                            )
                        }
                    }

                }


                Text(
                    text = "$${food?.Price ?: ""}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 20.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .height(50.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(50)
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.time_color),
                            contentDescription = "TimeValue",
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text("${food?.TimeValue} min", modifier = Modifier.padding(top = 3.dp))
                    }
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star",
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Crop,

                            )
                        Text("${food?.Star}", modifier = Modifier.padding(top = 3.dp))
                    }
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.flame),
                            contentDescription = "Calorie",
                            modifier = Modifier
                                .height(20.dp)
                                .padding(end = 5.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text("${food?.Calorie}", modifier = Modifier.padding(top = 3.dp))
                    }
                }
                Text(
                    "Details",
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Text(
                    text = food?.Description ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 20.dp)

                )
                Text(
                    text = "Buy 2 item for free delivery",
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(recommendedFoods) { food ->
                        AsyncImage(
                            model = food.ImagePath,
                            contentDescription = food.Title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    navController.navigate("details/${food.Id}")
                                }
                        )
                    }
                }
            }
        }


        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier.size(28.dp),
                tint = Color.Black
            )
        }

        IconButton(
            onClick = {
                if (isFavorite) {
                    viewModel.removeFavoriteFood(food?.Id ?: 0)
                } else {
                    viewModel.addFavoriteFood()
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.7f), shape = CircleShape)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(26.dp),
                tint = Color.Black
            )
        }

        if (food != null) {
            val currentFood = food!!
            val totalPrice = currentFood.Price * foodCount
            val actionColor = Color(0xFF087830)

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(actionColor)
                        .clickable { viewModel.addToOrder(currentFood, foodCount) }
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$${"%.2f".format(totalPrice)}",
                            color = actionColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = "Add to Cart",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_order),
                            contentDescription = "Add to Order",
                            tint = actionColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

