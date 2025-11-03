package com.example.aaa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aaa.ui.theme.AAATheme // <-- Sẽ tự động dùng Theme đúng của dự án

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AAATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Tạo NavController để quản lý việc điều hướng
                    val navController = rememberNavController()

                    // 2. Tạo NavHost để định nghĩa các đường đi (routes)
                    NavHost(
                        navController = navController,
                        startDestination = "root_screen" // Màn hình bắt đầu
                    ) {
                        // Route cho màn hình Root
                        composable(route = "root_screen") {
                            RootScreen(navController = navController)
                        }

                        // Route cho màn hình List
                        composable(route = "list_screen") {
                            ListScreen(navController = navController)
                        }

                        // Route cho màn hình Detail (có tham số là itemId)
                        composable(
                            route = "detail_screen/{itemId}",
                            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            // Lấy tham số itemId từ route
                            val itemId = backStackEntry.arguments?.getInt("itemId")
                            DetailScreen(navController = navController, itemId = itemId)
                        }
                    }
                }
            }
        }
    }
}