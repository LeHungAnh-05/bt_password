package com.example.bt_password.data.api

import com.example.bt_password.data.model.ApiResponse
import com.example.bt_password.data.model.Task
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // ✅ Lấy danh sách task
    @GET("tasks")
    fun getTasks(): Call<ApiResponse<List<Task>>>

    // ✅ Lấy chi tiết 1 task theo id
    @GET("task/{id}")
    fun getTaskById(@Path("id") id: Int): Call<ApiResponse<Task>>

    // ✅ Xóa task theo id
    @DELETE("task/{id}")
    fun deleteTask(@Path("id") id: Int): Call<ApiResponse<Void>>
}
