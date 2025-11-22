package com.example.uicomponentsapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uicomponentsapp.ui.theme.UIComponentsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Images") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Định nghĩa các URL
                val url1 = "https://uth.edu.vn/wp-content/uploads/2021/04/Toa-nha-trung-tam-truong-DH-GTVT-TPHCM.jpeg"
                val url2 = "https://diemthi.vnuhcm.edu.vn/static/img/school/GTS.jpg"
                val uriHandler = LocalUriHandler.current // Dùng để mở link

                // Ảnh thứ nhất
                Image(
                    painter = painterResource(id = R.drawable.uth),
                    contentDescription = "Ảnh trường ĐH GTVT TP.HCM",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                // LÀM CHO LINK CÓ THỂ BẤM VÀO
                Text(
                    text = url1,
                    modifier = Modifier.clickable { uriHandler.openUri(url1) }, // Thêm sự kiện click
                    style = TextStyle( // Thêm style để trông giống link
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )

                // Ảnh thứ hai
                Image(
                    painter = painterResource(id = R.drawable.uth2),
                    contentDescription = "Ảnh trường ĐH GTVT TP.HCM (2)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                // LÀM CHO LINK CÓ THỂ BẤM VÀO
                Text(
                    text = url2,
                    modifier = Modifier.clickable { uriHandler.openUri(url2) }, // Thêm sự kiện click
                    style = TextStyle( // Thêm style để trông giống link
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageScreenPreview() {
    UIComponentsAppTheme {
        ImageScreen {}
    }
}