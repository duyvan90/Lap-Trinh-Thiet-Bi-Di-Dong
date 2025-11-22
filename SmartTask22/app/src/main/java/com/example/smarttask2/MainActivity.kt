package com.example.smarttask2 // Đảm bảo dòng này đúng với package của bạn

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    // Khởi tạo Firebase
    private val auth by lazy { FirebaseAuth.getInstance() }

    // Launcher nhận kết quả đăng nhập Google
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.e("Auth", "Google sign in failed", e)
                Toast.makeText(this, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentUser by remember { mutableStateOf(auth.currentUser) }

            MaterialTheme {
                if (currentUser == null) {
                    // Màn hình Đăng nhập
                    LoginScreen(onSignInClick = { signInWithGoogle() })
                } else {
                    // Màn hình Profile
                    ProfileScreen(
                        user = currentUser!!,
                        onSignOutClick = {
                            auth.signOut()
                            currentUser = null
                            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        launcher.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    // Cập nhật UI
                    val user = auth.currentUser
                    setContent {
                        var currentUser by remember { mutableStateOf(user) }
                        ProfileScreen(
                            user = currentUser!!,
                            onSignOutClick = {
                                auth.signOut()
                                currentUser = null
                                recreate()
                            }
                        )
                    }
                } else {
                    Toast.makeText(this, "Lỗi xác thực Firebase", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

// --- CÁC MÀN HÌNH UI ---

@Composable
fun LoginScreen(onSignInClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // LOGO: Nếu chưa có ảnh, dòng này sẽ hiện placeholder
        // Image(painter = painterResource(id = R.drawable.logo_uth), ...)

        Text(text = "SmartTasks", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00695C))
        Text(text = "A simple and efficient to-do app", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "Ready to explore? Log in to get started.", fontSize = 14.sp, color = Color.DarkGray)

        Spacer(modifier = Modifier.height(16.dp))

        // NÚT ĐĂNG NHẬP GOOGLE ĐẸP
        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(4.dp),
            border = BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "G ", color = Color.Red, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign in with Google", color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun ProfileScreen(user: FirebaseUser, onSignOutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "←  Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Avatar
        AsyncImage(
            model = user.photoUrl,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Thông tin User
        UserInfoField(label = "Name", value = user.displayName ?: "No Name")
        UserInfoField(label = "Email", value = user.email ?: "No Email")
        UserInfoField(label = "Date of Birth", value = "23/05/1995") // Fake data

        Spacer(modifier = Modifier.weight(1f))

        // NÚT SIGN OUT (Màu xanh)
        Button(
            onClick = onSignOutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Sign Out", color = Color.White, fontSize = 16.sp)
        }
    }
}

// Hàm này lúc nãy bạn bị thiếu nên báo lỗi đỏ đây
@Composable
fun UserInfoField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}