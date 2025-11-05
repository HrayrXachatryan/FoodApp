package com.example.foodapp.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.Data.BottomNavItem
import com.example.foodapp.R
import com.example.foodapp.Retrofit.FoodsViewModel

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: FoodsViewModel = viewModel() // ✅ один экземпляр

    Scaffold(
        bottomBar = { BottomNavigationScreen(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController, viewModel) }
            composable("favorite") { FavoriteScreen(navController,viewModel) } // ✅ пробросили
            composable("order") { OrderScreen(viewModel) }
            composable("profile") {ProfileScreen() }

            // Детали продукта
            composable("details/{foodId}") { backStackEntry ->
                val foodId = backStackEntry.arguments?.getString("foodId")?.toIntOrNull()
                if (foodId != null) {
                    DetailsScreen(navController, foodId, viewModel) // ✅ пробросили
                }
            }
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Filled.Home),
        BottomNavItem("favorite", "Favorite", Icons.Filled.Favorite),
        BottomNavItem("order", "Order", null), // кастомная иконка
        BottomNavItem("profile", "Profile", Icons.Filled.Person),
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        // Чтобы не было дублирования при повторном нажатии
//                        popUpTo(navController.graph.startDestinationId) { saveState = true }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
                onClick = {
                    if (currentRoute == item.route) {

                        navController.popBackStack(item.route, inclusive = false)
                    } else {

                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },

                        icon = {
                    if (item.route == "order") {
                        Image(
                            painter = painterResource(id = R.drawable.ic_order),
                            contentDescription = "Order",
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        item.icon?.let { Icon(it, contentDescription = item.label) }
                    }
                },
                label = { Text(item.label) }
            )
        }
    }
}




