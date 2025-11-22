// Dòng 1: Đã sửa thành package "smarttask" của bạn
package com.example.smarttask

// Dòng 2: Imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Dòng 3: Đã sửa thành Theme "SmartTaskTheme" của bạn
import com.example.smarttask.ui.theme.SmartTaskTheme

// Dòng 4: Lớp MainActivity (Không đổi)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Dòng 5: Đã sửa thành Theme "SmartTaskTheme" của bạn
            SmartTaskTheme {
                // Khởi chạy luồng điều hướng
                PasswordResetFlow()
            }
        }
    }
}

// --- PHẦN MỚI: ĐỊNH NGHĨA CÁC ROUTE (ĐƯỜNG DẪN) ---
object Routes {
    const val FORGOT_PASSWORD = "forgot_password"
    const val VERIFICATION = "verification"
    const val RESET_PASSWORD = "reset_password"
    const val CONFIRM = "confirm"
}

// --- Composable chứa toàn bộ logic điều hướng ---
@Composable
fun PasswordResetFlow() {
    // 1. Tạo NavController
    val navController = rememberNavController()

    // 2. Tạo NavHost
    NavHost(
        navController = navController,
        startDestination = Routes.FORGOT_PASSWORD // Màn hình bắt đầu
    ) {
        // 3. Định nghĩa từng màn hình
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(Routes.VERIFICATION) {
            VerificationScreen(navController = navController)
        }
        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(navController = navController)
        }
        composable(Routes.CONFIRM) {
            ConfirmScreen(navController = navController)
        }
    }
}

// --- MÀN HÌNH 1: FORGOT PASSWORD ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoUTH() // Dùng logo tái sử dụng
        Spacer(modifier = Modifier.height(32.dp))
        Text("Forgot Password?", style = MaterialTheme.typography.headlineSmall)
        Text("Enter your Email, we will send you a verification code.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Chuyển sang màn hình "Verification"
                navController.navigate(Routes.VERIFICATION)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

// --- MÀN HÌNH 2: VERIFICATION ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(navController: NavController) {
    // State để lưu 6 chữ số (trong ảnh là 6 ô)
    var code by remember { mutableStateOf(List(6) { "" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoUTH()
        Spacer(modifier = Modifier.height(32.dp))
        Text("Verify Code", style = MaterialTheme.typography.headlineSmall)
        Text("Enter the the code we just sent you...", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        // 6 ô nhập code
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            code.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        // Chỉ cho phép nhập 1 ký tự
                        if (it.length <= 1) {
                            val newCode = code.toMutableList()
                            newCode[index] = it
                            code = newCode
                        }
                    },
                    modifier = Modifier.width(48.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Chuyển sang màn hình "Reset Password"
                navController.navigate(Routes.RESET_PASSWORD)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
    }
}

// --- MÀN HÌNH 3: RESET PASSWORD ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Dùng Scaffold để có TopAppBar (thanh trên cùng)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    // Nút quay lại (Back)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoUTH()
            Spacer(modifier = Modifier.height(32.dp))
            Text("Create new password", style = MaterialTheme.typography.headlineSmall)
            Text("Your new password must be different...", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                // Ẩn mật khẩu
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Chuyển sang màn hình "Confirm"
                    navController.navigate(Routes.CONFIRM)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}

// --- MÀN HÌNH 4: CONFIRM ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(navController: NavController) {
    // Dùng Scaffold để có TopAppBar (thanh trên cùng)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm") },
                navigationIcon = {
                    // Nút quay lại (Back)
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoUTH()
            Spacer(modifier = Modifier.height(32.dp))
            Text("Confirm", style = MaterialTheme.typography.headlineSmall)
            Text("We are here to help you!", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))

            // Các trường này bị vô hiệu hóa (disabled) để trông giống ảnh
            OutlinedTextField(
                value = "uth@gmail.com",
                onValueChange = { },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "123456", // Đây chỉ là placeholder
                onValueChange = { },
                label = { Text("ID (Ví dụ)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "*********",
                onValueChange = { },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Quay về màn hình đầu tiên của luồng
                    navController.popBackStack(Routes.FORGOT_PASSWORD, inclusive = false)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Trong ảnh là "Summit" (có thể gõ nhầm), "Submit" đúng hơn
                Text("Submit")
            }
        }
    }
}

// --- Composable TÁI SỬ DỤNG: Logo ---
@Composable
// --- Composable TÁI SỬ DỤNG: Logo ---
fun LogoUTH() {
    // Composable này sẽ căn giữa Logo và Chữ "SmartTasks" theo chiều dọc
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Dùng ảnh thật từ res/drawable
        Image(
            painter = painterResource(id = R.drawable.logo_uth),
            contentDescription = "UTH Logo",
            modifier = Modifier.size(150.dp) // Bạn có thể đổi kích thước này
        )

        // 2. Thêm chữ "SmartTasks" ngay bên dưới
        Text(
            "SmartTasks",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold, // Làm chữ đậm hơn một chút
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 8.dp) // Thêm chút khoảng cách
        )
    }
}