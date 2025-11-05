package com.example.foodapp.Retrofit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Data.Foods
import com.example.foodapp.Data.OrderItem
import com.example.foodapp.Data.RecommendedFoods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodsViewModel : ViewModel() {

    private val repository = ApiRepository()

    // --- Foods ---
    private val _foods = MutableStateFlow<List<Foods>>(emptyList())
    val foods: StateFlow<List<Foods>> get() = _foods

    // --- Search text ---
    // ViewModel
    private val _searchText = mutableStateOf("")
    val searchText: MutableState<String> get() = _searchText


    fun updateSearchText(text: String) {
        _searchText.value = text
    }

    // --- Food Details ---
    private val _foodDetails = MutableStateFlow<Foods?>(null)
    val foodDetails: StateFlow<Foods?> get() = _foodDetails

    // --- Food Count ---
    private val _foodsCount = MutableStateFlow(1)
    val foodsCount: StateFlow<Int> get() = _foodsCount

    fun incrementCount() {
        _foodsCount.value += 1
    }

    fun decrementCount() {
        _foodsCount.value = (_foodsCount.value - 1).coerceAtLeast(1)
    }

    fun resetCount() {
        _foodsCount.value = 1
    }

    // --- Recommended Foods ---
    private val _recommendedImagePath = MutableStateFlow<List<RecommendedFoods>>(emptyList())
    val recommendedImagePath: StateFlow<List<RecommendedFoods>> get() = _recommendedImagePath

    // --- Favorite Foods ---
    private val _favoriteFoods = MutableStateFlow<List<Foods>>(emptyList())
    val favoriteFoods: StateFlow<List<Foods>> get() = _favoriteFoods

    // --- Order Items ---
    private val _orderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderItems: StateFlow<List<OrderItem>> get() = _orderItems

    private val userFavorites = mutableMapOf<String, List<Foods>>()
    private val userOrders = mutableMapOf<String, List<OrderItem>>()

    private val guestUserKey = "guest"
    private var activeUserId: String? = null

    // -------------------- Fetching Data --------------------

    private suspend fun getAllFoods(): List<Foods> = repository.getData().Foods

    fun fetchFoods(categoryId: Int) {
        viewModelScope.launch {
            try {
                val allFoods = getAllFoods()
                _foods.value = if (categoryId == -1) allFoods else allFoods.filter { it.CategoryId == categoryId }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setActiveUser(userId: String?) {
        persistCurrentUserState()
        activeUserId = userId
        _favoriteFoods.value = userFavorites[currentUserKey()] ?: emptyList()
        _orderItems.value = userOrders[currentUserKey()] ?: emptyList()
    }

    fun fetchFoodDetails(foodId: Int) {
        viewModelScope.launch {
            try {
                val food = getAllFoods().find { it.Id == foodId }
                _foodDetails.value = food
                resetCount()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRecommendedFoods(limit: Int = 10) {
        viewModelScope.launch {
            try {
                val allFoods = getAllFoods()
                if (allFoods.isNotEmpty()) {
                    _recommendedImagePath.value = allFoods.shuffled()
                        .take(limit)
                        .map { RecommendedFoods(it.Id, it.Title, it.ImagePath) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addFavoriteFood() {
        _foodDetails.value?.let { food ->
            if (_favoriteFoods.value.none { it.Id == food.Id }) {
                updateFavorites(_favoriteFoods.value + food)
            }
        }
    }

    fun removeFavoriteFood(Id:Int) {
        updateFavorites(_favoriteFoods.value.filter { it.Id != Id })
    }

    fun addToOrder(food: Foods, quantity: Int) {
        if (quantity <= 0) return

        val existing = _orderItems.value.find { it.food.Id == food.Id }
        val updated = if (existing != null) {
            _orderItems.value.map {
                if (it.food.Id == food.Id) it.copy(quantity = it.quantity + quantity) else it
            }
        } else {
            _orderItems.value + OrderItem(food = food, quantity = quantity)
        }

        updateOrders(updated)
    }

    fun removeFromOrder(id: String) {
        updateOrders(_orderItems.value.filterNot { it.id == id })
    }

    fun updateOrderQuantity(id: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromOrder(id)
        } else {
            updateOrders(
                _orderItems.value.map {
                    if (it.id == id) it.copy(quantity = quantity) else it
                }
            )
        }
    }

    fun clearCurrentOrder() {
        updateOrders(emptyList())
    }

    private fun persistCurrentUserState() {
        userFavorites[currentUserKey()] = _favoriteFoods.value
        userOrders[currentUserKey()] = _orderItems.value
    }

    private fun updateFavorites(list: List<Foods>) {
        _favoriteFoods.value = list
        userFavorites[currentUserKey()] = list
    }

    private fun updateOrders(list: List<OrderItem>) {
        _orderItems.value = list
        userOrders[currentUserKey()] = list
    }

    private fun currentUserKey(): String = activeUserId ?: guestUserKey
}
