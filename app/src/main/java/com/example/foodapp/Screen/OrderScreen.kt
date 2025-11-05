package com.example.foodapp.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodapp.Data.OrderItem
import com.example.foodapp.R
import com.example.foodapp.Retrofit.FoodsViewModel

@Composable
fun OrderScreen(viewModel: FoodsViewModel = viewModel()) {
    val orderItems by viewModel.orderItems.collectAsState()

    if (orderItems.isEmpty()) {
        EmptyOrderState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(orderItems, key = { it.id }) { item ->
                OrderCard(
                    item = item,
                    onDelete = { id -> viewModel.removeFromOrder(id) }
                )
            }

            item {
                OrderSummary(
                    items = orderItems,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyOrderState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_order),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your order list is empty",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OrderCard(
    item: OrderItem,
    onDelete: (String) -> Unit
) {
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
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.food.ImagePath,
                contentDescription = item.food.Title,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(135.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.food.Title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Quantity: ${item.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Price per item: $${"%.2f".format(item.food.Price)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Total: $${"%.2f".format(item.food.Price * item.quantity)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            IconButton(onClick = { onDelete(item.id) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
private fun OrderSummary(
    items: List<OrderItem>,
    modifier: Modifier = Modifier
) {
    val subTotal = items.sumOf { it.food.Price * it.quantity }
    val deliveryFee = if (subTotal >= 20) 0.0 else 2.99
    val total = subTotal + deliveryFee

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SummaryRow(title = "Subtotal", value = subTotal)
            SummaryRow(title = "Delivery", value = deliveryFee)
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            SummaryRow(title = "Total", value = total, isTotal = true)
        }
    }
}

@Composable
private fun SummaryRow(title: String, value: Double, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$${"%.2f".format(value)}",
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (isTotal) Color.Black else Color.Gray
        )
    }
}
