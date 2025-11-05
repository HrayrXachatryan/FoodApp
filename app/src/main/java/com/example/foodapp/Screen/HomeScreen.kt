package com.example.foodapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodapp.R
import com.example.foodapp.Retrofit.FoodsViewModel





@Composable
fun HomeScreen(navController: NavController, viewModel: FoodsViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TopBar
        item {
            TopBar()
        }
        item { CategoryGridScreen() }
//        item { Text("Foods for you") }
        item { FoodsGridScreen(navController) }

    }
}

@Composable
fun TopBar(viewModel: FoodsViewModel = viewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp,end = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var textState by viewModel.searchText

        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier.height(56.dp),
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            placeholder = { Text(text = "What would you like to eat?" ,fontSize = 10.sp) },
            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .width(220.dp)
                .height(56.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.LightGray
            )
        )

        IconButton(
            onClick = { },
            modifier = Modifier
                .size(50.dp)
                .background(color = Color.White, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

