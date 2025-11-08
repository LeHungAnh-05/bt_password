package com.example.bt_password.data.model

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val dueDate: String?,
    val priority: String?,
    val category: String?,
    val subtasks: List<Subtask>?,
    val attachments: List<Attachment>?
)
