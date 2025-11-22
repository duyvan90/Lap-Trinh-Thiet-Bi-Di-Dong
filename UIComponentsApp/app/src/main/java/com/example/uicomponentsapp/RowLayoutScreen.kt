package com.example.uicomponentsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uicomponentsapp.ui.theme.UIComponentsAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RowLayoutScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Row Layout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // FlowRow sẽ tự động xuống hàng khi không đủ chỗ
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tạo 12 ô vuông màu xanh
            repeat(12) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 60.dp)
                        .background(Color(0xFF4285F4))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RowLayoutScreenPreview() {
    UIComponentsAppTheme {
        RowLayoutScreen {}
    }
}