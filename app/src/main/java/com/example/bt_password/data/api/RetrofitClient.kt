package com.example.bt_password.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // ✅ Base URL chỉ đến thư mục gốc, phải có dấu / ở cuối
    private const val BASE_URL = "https://amock.io/api/researchUTH/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
