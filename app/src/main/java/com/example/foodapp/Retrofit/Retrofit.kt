package com.example.foodapp.Retrofit

import com.example.foodapp.Data.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("database.json")
    suspend fun getData(): ApiResponse
}


object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/worldsat/project241/main/") // 👈 до папки
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class ApiRepository {

    private val apiService = RetrofitInstance.api

    suspend fun getData(): ApiResponse {
        return apiService.getData()
    }
}