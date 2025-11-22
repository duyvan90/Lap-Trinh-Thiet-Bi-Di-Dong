package com.example.smarttask3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smarttask3.ui.screen.ProductScreen
import com.example.smarttask3.ui.theme.SmartTask3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartTask3Theme {
                // Gọi màn hình ProductScreen vào đây
                ProductScreen()
            }
        }
    }
}