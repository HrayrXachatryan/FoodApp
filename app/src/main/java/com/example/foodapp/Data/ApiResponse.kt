package com.example.foodapp.Data

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("Category") val Category: List<Category>,
    @SerializedName("Foods") val Foods: List<Foods>
)

