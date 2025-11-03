package com.example.lyt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyLayout()
                }
            }
        }
    }
}

@Composable
fun MyLayout() {
    var selectedText by remember { mutableStateOf("Chưa chọn nút nào") }

    // Bố cục dọc tổng thể (Column)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Tiêu đề
        Text(
            text = "Bố cục Row + Column",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Bố cục hàng (Row)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedText = "Bạn đã chọn: Trái" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
            ) {
                Text("Trái")
            }

            Button(
                onClick = { selectedText = "Bạn đã chọn: Giữa" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Giữa")
            }

            Button(
                onClick = { selectedText = "Bạn đã chọn: Phải" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Phải")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Văn bản hiển thị kết quả
        Text(
            text = selectedText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
