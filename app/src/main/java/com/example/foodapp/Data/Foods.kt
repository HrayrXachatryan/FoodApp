package com.example.foodapp.Data

data class Foods(
    val Id: Int,
    val Title: String,
    val Description: String,
    val ImagePath: String,
    val Price: Double,
    val Calorie: Int,
    val Star: Double,
    val BestFood: Boolean,
    val CategoryId: Int,
    val LocationId: Int,
    val PriceId: Int,
    val TimeId: Int,
    val TimeValue: Int
)

data class RecommendedFoods(
    val Id: Int,
    val Title: String,
    val ImagePath: String,

)