package com.example.foodapp.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.Retrofit.FoodsViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FoodsViewModel
){
    val favoriteFoods by viewModel.favoriteFoods.collectAsState()

    if (favoriteFoods.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No foods selected yet",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }else{
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ){
            items(favoriteFoods){food->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(135.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = food.ImagePath,
                            contentDescription = food.Title,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(135.dp)
                                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                                .clickable {
                                    navController.navigate("details/${food.Id}")
                                },
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = food.Title,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
                            )
                            Row(
                                modifier = Modifier.padding(bottom = 5.dp),

                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.time),
                                    contentDescription = "time",
                                    modifier = Modifier
                                        .size(12.dp)
                                        .padding(top = 3.dp)
                                )
                                Text(text = "${food.TimeValue} min")
                            }
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


                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(text = "$${food.Price}", style = MaterialTheme.typography.bodyMedium)

                                IconButton(onClick = {
                                    viewModel.removeFavoriteFood(food.Id)
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "delete"
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}