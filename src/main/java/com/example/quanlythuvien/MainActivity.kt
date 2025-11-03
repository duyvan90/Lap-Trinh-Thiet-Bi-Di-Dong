// Dòng 1: Giữ nguyên package name của BẠN
package com.example.quanlythuvien

// Dòng 2: Import thêm các thư viện cần thiết
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlin.random.Random

// --- (Toàn bộ import từ code cũ của tôi) ---
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dòng 3: Import Theme của project
import com.example.quanlythuvien.ui.theme.QuanLyThuVienTheme

// Dòng 4: Lớp MainActivity (Phần khung sườn)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuanLyThuVienTheme {
                LibraryManagementScreen()
            }
        }
    }
}

// --- BẮT ĐẦU CODE BÀI TẬP CẬP NHẬT ---

// --- 1. Định nghĩa cấu trúc dữ liệu ---
data class Book(val id: String, val title: String)
data class Student(val id: String, val name: String, val borrowedBooks: List<Book>)

// --- 2. Dữ liệu ban đầu (thay vì dữ liệu cố định) ---
val initialStudents = listOf(
    Student(
        id = "SV001",
        name = "Nguyen Van A",
        borrowedBooks = listOf(
            Book("S01", "Sách 01"),
            Book("S02", "Sách 02")
        )
    ),
    Student(
        id = "SV002",
        name = "Nguyen Thi B",
        borrowedBooks = listOf(
            Book("S01", "Sách 01")
        )
    ),
    Student(
        id = "SV003",
        name = "Nguyen Van C",
        borrowedBooks = emptyList()
    )
)

// --- 3. Màn hình chính (Composable) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryManagementScreen() {

    // --- NÂNG CẤP QUAN TRỌNG ---
    // Giờ đây 'students' là một "trạng thái" (state).
    // Khi state này thay đổi, giao diện sẽ tự động cập nhật.
    var students by remember { mutableStateOf(initialStudents) }

    // Vẫn dùng 'selectedStudentIndex' để biết đang xem sinh viên nào
    var selectedStudentIndex by remember { mutableStateOf(0) }

    // Lấy sinh viên hiện tại, có thể là null nếu danh sách rỗng
    val currentStudent = students.getOrNull(selectedStudentIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hệ thống Quản lý Thư viện") }
            )
        },
        bottomBar = {
            AppBottomNavigation()
        },
        // Nút "Thêm" (Thêm SÁCH cho sinh viên hiện tại)
        floatingActionButton = {
            Button(
                onClick = {
                    // Chỉ thêm sách nếu có sinh viên đang được chọn
                    currentStudent?.let { student ->
                        // 1. Tạo một cuốn sách ngẫu nhiên
                        val bookNumber = (1..99).random()
                        val newBook = Book("S$bookNumber", "Sách ngẫu nhiên $bookNumber")

                        // 2. Tạo bản sao của sinh viên hiện tại với sách mới
                        val updatedStudent = student.copy(
                            borrowedBooks = student.borrowedBooks + newBook
                        )

                        // 3. Cập nhật danh sách students
                        // (thay thế sinh viên cũ bằng sinh viên đã cập nhật)
                        students = students.toMutableList().apply {
                            set(selectedStudentIndex, updatedStudent)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Thêm Sách", fontSize = 18.sp)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Xử lý trường hợp không có sinh viên nào
            if (students.isEmpty()) {
                Text(
                    "Không có sinh viên nào trong hệ thống. Hãy thêm mới!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Phần chọn Sinh viên
            StudentSection(
                // Gửi tên SV, nếu null thì hiển thị "..."
                studentName = currentStudent?.name ?: "...",

                // Nút "Thay đổi" chỉ bật khi có nhiều hơn 1 SV
                isChangeEnabled = students.size > 1,

                // Xử lý khi nhấn "Thay đổi"
                onChangeClick = {
                    if (students.isNotEmpty()) {
                        selectedStudentIndex = (selectedStudentIndex + 1) % students.size
                    }
                },

                // Xử lý khi nhấn "Thêm SV"
                onAddStudentClick = {
                    // 1. Tạo sinh viên mới ngẫu nhiên
                    val studentNumber = students.size + 1
                    val newStudentName = "Sinh viên $studentNumber"
                    val newStudent = Student(
                        id = "SV$studentNumber",
                        name = newStudentName,
                        borrowedBooks = emptyList() // SV mới chưa mượn sách
                    )

                    // 2. Thêm SV mới vào danh sách 'students'
                    // Phép cộng này tạo ra 1 list mới, làm state thay đổi
                    students = students + newStudent

                    // 3. Tự động chọn SV vừa thêm
                    selectedStudentIndex = students.size - 1
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Phần danh sách sách
            // Gửi danh sách sách, có thể là null nếu chưa có SV
            BookListSection(books = currentStudent?.borrowedBooks)
        }
    }
}

// --- 4. Composable cho phần Sinh viên (CẬP NHẬT) ---
@Composable
fun StudentSection(
    studentName: String,
    isChangeEnabled: Boolean, // Thêm biến để bật/tắt nút "Thay đổi"
    onChangeClick: () -> Unit,
    onAddStudentClick: () -> Unit // Thêm callback cho nút "Thêm SV"
) {
    Text("Sinh viên", style = MaterialTheme.typography.titleMedium)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = studentName,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Nút "Thay đổi"
        Button(onClick = onChangeClick, enabled = isChangeEnabled) {
            Text("Đổi SV") // Rút gọn chữ
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nút "Thêm SV" MỚI
        Button(onClick = onAddStudentClick) {
            Text("Thêm SV")
        }
    }
}

// --- 5. Composable cho phần Danh sách sách (CẬP NHẬT) ---
@Composable
fun BookListSection(books: List<Book>?) { // 'books' giờ có thể là null
    Text("Danh sách sách", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Kiểm tra logic: null hoặc rỗng
        if (books.isNullOrEmpty()) {
            Text(
                text = "Bạn chưa mượn quyển sách nào\nNhấn 'Thêm Sách' để bắt đầu!",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Có sách -> Hiển thị LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(books) { book ->
                    BookItem(book)
                }
            }
        }
    }
}

// --- 6. Composable cho một Sách trong danh sách (Không đổi) ---
@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = {}
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(book.title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// --- 7. Composable cho Thanh điều hướng dưới (Không đổi) ---
@Composable
fun AppBottomNavigation() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Quản lý") },
            label = { Text("Quản lý") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Book, contentDescription = "DS Sách") },
            label = { Text("DS Sách") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Sinh viên") },
            label = { Text("Sinh viên") },
            selected = false,
            onClick = {}
        )
    }
}