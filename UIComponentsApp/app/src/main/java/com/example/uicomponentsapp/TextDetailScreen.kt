package com.example.uicomponentsapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// ĐẢM BẢO TẤT CẢ CÁC IMPORT NÀY LÀ TỪ ".material3"
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uicomponentsapp.ui.theme.UIComponentsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDetailScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Text Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("The ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append("quick")
                    }

                    append(" ")

                    withStyle(style = SpanStyle(
                        color = Color(0xFFDAA520),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )) {
                        append("Brown")
                    }

                    append(" fox ")

                    withStyle(style = SpanStyle(letterSpacing = 8.sp)) {
                        append("jumps")
                    }

                    append(" ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("over")
                    }

                    append(" ")

                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("the")
                    }

                    append(" ")

                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append("lazy")
                    }

                    append(" dog.")
                },
                fontSize = 24.sp,
                lineHeight = 40.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextDetailScreenPreview() {
    UIComponentsAppTheme {
        TextDetailScreen(onBackClick = {})
    }
}