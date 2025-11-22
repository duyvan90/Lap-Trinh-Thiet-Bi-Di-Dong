package com.example.smarttask4.data.model

data class TaskResponse(
    val id: String? = "1", // Cho phép null, mặc định là 1
    val title: String? = "Task Title",
    val description: String? = "Description goes here",
    val status: String? = "Pending",
    val priority: String? = "Low",
    val category: String? = "General",
    val dueDate: String? = "2025-01-01",
    val colorCode: String? = "#E3F2FD"
)