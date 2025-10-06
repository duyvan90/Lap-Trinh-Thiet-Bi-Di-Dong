package com.example.nhapso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhapso.ui.theme.NhapSoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NhapSoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NumberInputScreen()
                }
            }
        }
    }
}

@Composable
fun NumberInputScreen() {
    var inputText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var numberList by remember { mutableStateOf(listOf<Int>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Thực hành 02",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Row: ô nhập + nút "Tạo"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = {
                    inputText = it
                    // khi thay đổi input, xóa lỗi và kết quả cũ
                    errorMessage = ""
                    numberList = emptyList()
                },
                placeholder = { Text("Nhập vào số lượng") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    // chuyển sang Int, nếu null => lỗi
                    val n = inputText.toIntOrNull()
                    if (n == null || n <= 0) {
                        errorMessage = "Dữ liệu bạn nhập không hợp lệ ❗"
                        numberList = emptyList()
                    } else {
                        errorMessage = ""
                        numberList = (1..n).toList()
                    }
                },
                modifier = Modifier
                    .height(52.dp)
            ) {
                Text(text = "Tạo")
            }
        }

        // Hiển thị lỗi (nếu có)
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Danh sách kết quả (các ô đỏ)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            numberList.forEach { num ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(
                            color = Color(0xFFE53935), // đỏ
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = num.toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
