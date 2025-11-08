package com.example.bt_password.data.model

data class ApiResponse<T>(
    val isSuccess: Boolean,
    val message: String,
    val data: T
)
