package com.example.smarttask4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttask4.data.room.AppDatabase
import com.example.smarttask4.data.room.TaskEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).taskDao()

    // Danh sách task (Tự động cập nhật)
    val allTasks = dao.getAllTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Hàm thêm task mới
    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            // Random màu sắc cho đẹp giống hình mẫu
            val colors = listOf("#FFCDD2", "#DCEDC8", "#B3E5FC", "#FFF9C4")
            val randomColor = colors.random()

            val newTask = TaskEntity(
                title = title,
                description = description,
                colorCode = randomColor
            )
            dao.insertTask(newTask)
        }
    }

    // Hàm xóa task
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            dao.deleteTask(task)
        }

    }
    fun onTaskChecked(task: TaskEntity, isChecked: Boolean) {
        viewModelScope.launch {
            // Tạo bản sao của task với trạng thái mới
            val updatedTask = task.copy(isCompleted = isChecked)
            dao.updateTask(updatedTask)
        }
    }


}

