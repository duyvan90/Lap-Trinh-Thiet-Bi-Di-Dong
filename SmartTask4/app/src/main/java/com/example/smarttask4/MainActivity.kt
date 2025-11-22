package com.example.smarttask4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smarttask4.ui.screen.AddScreen
import com.example.smarttask4.ui.screen.DetailScreen
import com.example.smarttask4.ui.screen.HomeScreen
import com.example.smarttask4.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            // Khởi tạo ViewModel
            val taskViewModel: TaskViewModel = viewModel()

            NavHost(navController = navController, startDestination = "home") {

                // Màn hình chính (Danh sách từ DB)
                composable("home") {
                    HomeScreen(navController, taskViewModel)
                }

                // Màn hình thêm mới (Lưu vào DB)
                composable("add") {
                    AddScreen(navController, taskViewModel)
                }

                // Màn hình chi tiết (Gọi API - Cũ)
                composable("detail/{taskId}") { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getString("taskId") ?: "1"
                    DetailScreen(navController, taskId)
                }
            }
        }
    }
}