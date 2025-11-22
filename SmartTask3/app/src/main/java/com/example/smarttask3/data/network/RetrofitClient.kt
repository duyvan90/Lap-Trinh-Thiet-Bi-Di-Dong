package com.example.smarttask3.data.network

import com.example.smarttask3.data.model.ProductResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Định nghĩa API
interface ApiService {
    @GET("v2/product")
    suspend fun getProduct(): ProductResponse
}

// Cấu hình Retrofit
object RetrofitClient {
    private const val BASE_URL = "https://mock.apidog.com/m1/890655-872447-default/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}