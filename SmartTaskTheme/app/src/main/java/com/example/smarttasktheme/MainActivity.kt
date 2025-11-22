package com.example.smarttasktheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Khởi tạo ThemeStore
            val context = LocalContext.current
            val themeStore = ThemeStore(context)

            // Lắng nghe dữ liệu từ DataStore (Màu đã lưu)
            // collectAsState giúp UI tự động đổi màu khi dữ liệu thay đổi
            val savedTheme by themeStore.themeFlow.collectAsState(initial = "Blue")

            ThemeScreen(
                currentTheme = savedTheme,
                onApplyTheme = { newColor ->
                    // Gọi hàm lưu vào DataStore (chạy trong Coroutine)
                    // Lưu ý: Trong Compose dùng LaunchedEffect hoặc CoroutineScope để chạy suspend fun
                    // Nhưng ở đây ta truyền event ra ngoài hoặc dùng scope tại chỗ
                },
                themeStore = themeStore // Truyền store vào để lưu
            )
        }
    }
}

@Composable
fun ThemeScreen(currentTheme: String, onApplyTheme: (String) -> Unit, themeStore: ThemeStore) {
    // Scope để chạy lệnh lưu
    val scope = rememberCoroutineScope()

    // Biến tạm để lưu màu đang chọn (nhưng chưa bấm Apply)
    var selectedOption by remember { mutableStateOf(currentTheme) }

    // Cập nhật selectedOption khi currentTheme thay đổi (lúc mới mở app)
    LaunchedEffect(currentTheme) {
        selectedOption = currentTheme
    }

    // Map chuyển đổi từ Tên màu sang Mã màu thực tế
    fun getColor(name: String): Color {
        return when (name) {
            "Blue" -> Color(0xFF64B5F6) // Xanh dương nhạt
            "Pink" -> Color(0xFFF06292) // Hồng
            "Dark" -> Color(0xFF212121) // Đen xám
            else -> Color(0xFF64B5F6)
        }
    }

    // Giao diện chính
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getColor(currentTheme)), // Nền thay đổi theo màu ĐÃ LƯU (Apply xong mới đổi)
        contentAlignment = Alignment.Center
    ) {
        // Cái thẻ trắng ở giữa
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Setting",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64B5F6)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Choosing the right theme for your app",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 3 Ô màu để chọn
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ColorOption("Blue", Color(0xFF64B5F6), selectedOption == "Blue") { selectedOption = "Blue" }
                    ColorOption("Pink", Color(0xFFF06292), selectedOption == "Pink") { selectedOption = "Pink" }
                    ColorOption("Dark", Color(0xFF212121), selectedOption == "Dark") { selectedOption = "Dark" }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Nút Apply
                Button(
                    onClick = {
                        // Khi bấm Apply -> Lưu vào DataStore
                        scope.launch {
                            themeStore.saveTheme(selectedOption)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2979FF))
                ) {
                    Text("Apply", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Component con: Một ô màu tròn
@Composable
fun ColorOption(name: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp) // Kích thước ô màu
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .border(
                width = if (isSelected) 3.dp else 0.dp, // Nếu chọn thì có viền đen
                color = if (isSelected) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    )
}