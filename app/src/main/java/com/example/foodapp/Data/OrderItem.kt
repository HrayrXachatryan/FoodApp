package com.example.foodapp.Data

import java.util.UUID

data class OrderItem(
    val id: String = UUID.randomUUID().toString(),
    val food: Foods,
    val quantity: Int
)
