package com.example.smarttask4.data.network

import com.example.smarttask4.data.model.TaskResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

// ĐỊNH NGHĨA CÁC HÀM GỌI API (Giống hệt đề bài)
interface TaskApiService {
    @GET("tasks")
    suspend fun getTasks(): List<TaskResponse>

    @GET("task/{id}")
    suspend fun getTaskDetail(@Path("id") id: String): TaskResponse

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: String)
}

object TaskRetrofitClient {
    // LINK API GỐC (Giống hệt đề bài)
    private const val BASE_URL = "https://amock.io/api/researchUTH/"

    val api: TaskApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApiService::class.java)
    }
}