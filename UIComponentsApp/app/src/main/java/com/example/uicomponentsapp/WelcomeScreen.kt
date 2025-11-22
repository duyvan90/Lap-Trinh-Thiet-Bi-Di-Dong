package com.example.uicomponentsapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uicomponentsapp.ui.theme.UIComponentsAppTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
@Composable
fun WelcomeScreen(onReadyClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lưu ý: Phần code logo đang được tạm ẩn.
        // Để hiển thị, hãy thêm ảnh vào res/drawable và bỏ comment

        Image(
            painter = painterResource(id = R.drawable.jetpackcompose_logo),
            contentDescription = "Jetpack Compose Logo",
            modifier = Modifier.size(400.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Jetpack Compose",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1f)) // Dùng weight để đẩy nút xuống dưới

        Button(
            onClick = onReadyClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("I'm ready")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    UIComponentsAppTheme {
        WelcomeScreen(onReadyClick = {})
    }
}