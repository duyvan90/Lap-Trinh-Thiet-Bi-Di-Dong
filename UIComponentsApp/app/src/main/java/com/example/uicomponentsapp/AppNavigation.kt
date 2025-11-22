package com.example.uicomponentsapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        // Màn hình chào mừng (không đổi)
        composable(route = "welcome") {
            WelcomeScreen(onReadyClick = {
                navController.navigate("component_list")
            })
        }

        // Màn hình danh sách (cập nhật logic click)
        composable(route = "component_list") {
            ComponentListScreen(onComponentClick = { componentName ->
                // Dùng when để xử lý các trường hợp click
                when (componentName) {
                    "Text" -> navController.navigate("text_detail")
                    "Image" -> navController.navigate("image_detail")
                    "TextField" -> navController.navigate("textfield_detail")
                    "Row" -> navController.navigate("row_layout_detail")
                    // Các trường hợp khác có thể thêm ở đây
                }
            })
        }

        // Màn hình chi tiết Text (không đổi)
        composable(route = "text_detail") {
            TextDetailScreen(onBackClick = {
                navController.popBackStack()
            })
        }

        // Thêm route cho màn hình Image
        composable(route = "image_detail") {
            ImageScreen(onBackClick = {
                navController.popBackStack()
            })
        }

        // Thêm route cho màn hình TextField
        composable(route = "textfield_detail") {
            TextFieldScreen(onBackClick = {
                navController.popBackStack()
            })
        }

        // Thêm route cho màn hình Row Layout
        composable(route = "row_layout_detail") {
            RowLayoutScreen(onBackClick = {
                navController.popBackStack()
            })
        }
    }
}