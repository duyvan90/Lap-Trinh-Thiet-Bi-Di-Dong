package com.example.smarttask3.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
// Import quan trọng để sửa lỗi 'by remember'
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.smarttask3.data.model.ProductResponse
import com.example.smarttask3.data.network.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen() {
    // Khai báo biến state
    var product by remember { mutableStateOf<ProductResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Gọi API
    LaunchedEffect(Unit) {
        try {
            product = RetrofitClient.apiService.getProduct()
            isLoading = false
        } catch (e: Exception) {
            Log.e("API_ERROR", e.toString())
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product detail", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF2196F3))
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                product?.let { ProductContent(it) }
            }
        }
    }
}

@Composable
fun ProductContent(product: ProductResponse) {
    // 1. Ảnh dự phòng (như cũ)
    val fallbackImage = "https://img.freepik.com/free-photo/pair-trainers_144627-3800.jpg"
    val imageUrl = if (product.image.isNullOrEmpty()) fallbackImage else product.image

    // 2. ĐOẠN VĂN MẪU (Lấy từ hình đề bài của bạn)
    val sampleDescription = "Với giày chạy bộ, từng gram đều quan trọng. Đó là lý do tại sao đế giày LIGHTSTRIKE PRO mới nhẹ hơn so với phiên bản trước. Mút foam đế giữa siêu nhẹ và thoải mái này có lớp đệm đàn hồi được thiết kế để hạn chế tiêu hao năng lượng. Trong các mẫu giày tập luyện, công nghệ này được thiết kế nhằm hỗ trợ cơ bắp của vận động viên để họ có thể phục hồi nhanh hơn giữa các cuộc đua."

    // Nếu API không trả về mô tả, dùng đoạn văn mẫu ở trên
    val finalDescription = if (product.description.isNullOrEmpty()) sampleDescription else product.description

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Shoe Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name ?: "Sản phẩm mẫu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Giá: ${String.format("%,.0f", product.price ?: 0.0)}đ",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Khung chứa mô tả
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            Text(
                text = finalDescription ?: "", // Hiển thị đoạn văn mẫu
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Justify // Căn đều 2 bên cho đẹp
            )
        }
    }
}