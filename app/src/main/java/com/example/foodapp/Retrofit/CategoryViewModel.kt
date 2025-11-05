package com.example.foodapp.Retrofit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Data.Category
import com.example.foodapp.Data.Foods
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _selectedCategoryId = mutableIntStateOf(-1)
    val selectedCategoryId : MutableState<Int> = _selectedCategoryId

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getData()
                _categories.value = response.Category
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}
