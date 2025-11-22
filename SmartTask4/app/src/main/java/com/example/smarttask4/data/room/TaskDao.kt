package com.example.smarttask4.data.room

import androidx.room.Update
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Lấy tất cả danh sách (Flow giúp tự động cập nhật UI khi data đổi)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    // Thêm mới
    @Insert
    suspend fun insertTask(task: TaskEntity)

    // Xóa
    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)
}