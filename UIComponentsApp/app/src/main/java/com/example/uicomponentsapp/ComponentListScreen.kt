package com.example.uicomponentsapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uicomponentsapp.ui.theme.UIComponentsAppTheme

// Cấu trúc dữ liệu cho một item (Không thay đổi)
data class UIComponent(val name: String, val description: String, val group: String)

// Dữ liệu mẫu (Không thay đổi)
val components = listOf(
    UIComponent("Text", "Displays text", "Display"),
    UIComponent("Image", "Displays an image", "Display"),
    UIComponent("TextField", "Input field for text", "Input"),
    UIComponent("PasswordField", "Input field for passwords", "Input"),
    UIComponent("Column", "Arranges elements vertically", "Layout"),
    UIComponent("Row", "Arranges elements horizontally", "Layout"),
    UIComponent("Tự tìm hiểu", "Tìm tòi cả các thành phần UI Cơ bản", "Custom") // Thêm mục mới
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentListScreen(onComponentClick: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("UI Components List") },
                // 1. CĂN GIỮA VÀ ĐỔI MÀU HEADER
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4285F4), // Màu xanh Google
                    titleContentColor = Color.White      // Chữ màu trắng
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            components.groupBy { it.group }.forEach { (group, itemsInGroup) ->
                item {
                    // 3. PHÓNG TO TIÊU ĐỀ NHÓM
                    Text(
                        text = group,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
                items(itemsInGroup) { component ->
                    ComponentItem(component, onClick = { onComponentClick(component.name) })
                }
            }
        }
    }
}

@Composable
fun ComponentItem(component: UIComponent, onClick: () -> Unit) {
    // 2. THÊM VIỀN XANH CHO CÁC MỤC CON
    val itemModifier = Modifier
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .border(
            width = 1.dp,
            color = Color(0xFF4285F4).copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp)
        )
        .clickable(onClick = onClick)
        .padding(horizontal = 16.dp, vertical = 12.dp)

    Column(
        modifier = itemModifier.fillMaxWidth()
    ) {
        Text(text = component.name, style = MaterialTheme.typography.bodyLarge)
        Text(text = component.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Preview(showBackground = true)
@Composable
fun ComponentListScreenPreview() {
    UIComponentsAppTheme {
        ComponentListScreen(onComponentClick = {})
    }
}