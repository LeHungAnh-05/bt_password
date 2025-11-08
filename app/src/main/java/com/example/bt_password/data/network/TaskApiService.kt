package com.example.bt_password.data.network

import com.example.bt_password.data.model.Task
import retrofit2.http.*

interface TaskApiService {
    @GET("researchUTH/tasks")
    suspend fun getTasks(): List<Task>

    @GET("researchUTH/task/{id}")
    suspend fun getTaskDetail(@Path("id") id: Int): Task

    @DELETE("researchUTH/task/{id}")
    suspend fun deleteTask(@Path("id") id: Int)
}
