// Dòng 1: Giữ nguyên package name của BẠN
package com.example.quanlythuvien // (Hoặc tên project của bạn)

// Dòng 2: Imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlin.random.Random

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dòng 3: Import Theme của project
import com.example.quanlythuvien.ui.theme.QuanLyThuVienTheme // (Hoặc tên Theme của bạn)

// Dòng 4: Lớp MainActivity (Không đổi)
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

// --- 1. Định nghĩa cấu trúc dữ liệu (Không đổi) ---
data class Book(val id: String, val title: String)
data class Student(val id: String, val name: String, val borrowedBooks: List<Book>)

// --- 2. Dữ liệu ban đầu ---

// Kho 20 cuốn sách cố định của thư viện
val masterBookList = (1..20).map {
    Book(id = "S$it", title = "Sách Thư Viện $it")
}

// Danh sách sinh viên ban đầu
val initialStudents = listOf(
    Student(
        id = "SV001",
        name = "Nguyen Van A",
        borrowedBooks = listOf(masterBookList[0], masterBookList[1]) // Mượn 2 cuốn đầu
    ),
    Student(
        id = "SV002",
        name = "Nguyen Thi B",
        borrowedBooks = listOf(masterBookList[2]) // Mượn cuốn thứ 3
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

    var students by remember { mutableStateOf(initialStudents) }
    var selectedStudentIndex by remember { mutableStateOf(0) }
    var showAddStudentDialog by remember { mutableStateOf(false) }

    val currentStudent = students.getOrNull(selectedStudentIndex)

    // NÂNG CẤP: Lấy danh sách tất cả sách đã được mượn bởi BẤT KỲ AI
    val allBorrowedBooks = students.flatMap { it.borrowedBooks }.toSet()
    // Lấy danh sách sách SV hiện tại mượn (để check nhanh)
    val currentStudentBooks = currentStudent?.borrowedBooks?.toSet() ?: emptySet()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hệ thống Quản lý Thư viện") }
            )
        },
        bottomBar = {
            AppBottomNavigation()
        }
        // NÂNG CẤP: Xóa floatingActionButton (nút "Thêm Sách" ở dưới)
        // vì chức năng giờ đã tích hợp vào danh sách

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // ... (Phần xử lý students.isEmpty() giữ nguyên) ...

            // Phần chọn Sinh viên
            StudentSection(
                studentName = currentStudent?.name ?: "...",
                isChangeEnabled = students.size > 1,
                onChangeClick = {
                    if (students.isNotEmpty()) {
                        selectedStudentIndex = (selectedStudentIndex + 1) % students.size
                    }
                },
                onAddStudentClick = {
                    showAddStudentDialog = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // NÂNG CẤP: Phần danh sách sách
            // Giờ sẽ hiển thị toàn bộ 20 cuốn sách
            BookListSection(
                libraryBooks = masterBookList,
                borrowedByCurrentStudent = currentStudentBooks,
                borrowedByAnyone = allBorrowedBooks,
                onBookBorrow = { bookToBorrow ->
                    // Logic khi nhấn nút "Mượn"
                    currentStudent?.let { student ->
                        // Thêm sách vào danh sách của sinh viên
                        val updatedStudent = student.copy(
                            borrowedBooks = student.borrowedBooks + bookToBorrow
                        )
                        // Cập nhật lại state 'students'
                        students = students.toMutableList().apply {
                            set(selectedStudentIndex, updatedStudent)
                        }
                    }
                }
            )
        }

        // (Phần Dialog Thêm Sinh viên giữ nguyên)
        if (showAddStudentDialog) {
            AddStudentDialog(
                onDismiss = { showAddStudentDialog = false },
                onAdd = { newName ->
                    val studentNumber = students.size + 1
                    val newStudent = Student(
                        id = "SV$studentNumber",
                        name = newName,
                        borrowedBooks = emptyList()
                    )
                    students = students + newStudent
                    selectedStudentIndex = students.size - 1
                    showAddStudentDialog = false
                }
            )
        }
    }
}

// --- 4. Composable cho phần Sinh viên (Không đổi) ---
@Composable
fun StudentSection(
    studentName: String,
    isChangeEnabled: Boolean,
    onChangeClick: () -> Unit,
    onAddStudentClick: () -> Unit
) {
    // ... (Code giữ nguyên) ...
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
        Button(onClick = onChangeClick, enabled = isChangeEnabled) {
            Text("Đổi SV")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onAddStudentClick) {
            Text("Thêm SV")
        }
    }
}

// --- 5. Composable cho phần Danh sách sách (CẬP NHẬT) ---
@Composable
fun BookListSection(
    libraryBooks: List<Book>, // Toàn bộ 20 cuốn
    borrowedByCurrentStudent: Set<Book>, // Sách SV này mượn
    borrowedByAnyone: Set<Book>, // Sách bất kỳ ai mượn
    onBookBorrow: (Book) -> Unit // Hàm để xử lý khi nhấn "Mượn"
) {
    Text("Sách trong thư viện (20)", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    // Box xám chứa danh sách
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Giữ nguyên chiều cao cố định
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Hiển thị toàn bộ 20 cuốn sách
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(libraryBooks) { book ->
                // Xác định trạng thái của từng cuốn sách
                val isBorrowedByCurrent = book in borrowedByCurrentStudent
                val isAvailable = book !in borrowedByAnyone

                // Dùng Composable BookItem mới
                BookItem(
                    book = book,
                    isBorrowedByCurrent = isBorrowedByCurrent,
                    isAvailable = isAvailable,
                    onBorrowClick = { onBookBorrow(book) }
                )
            }
        }
    }
}

// --- 6. Composable cho một Sách trong danh sách (CẬP NHẬT) ---
@Composable
fun BookItem(
    book: Book,
    isBorrowedByCurrent: Boolean,
    isAvailable: Boolean,
    onBorrowClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Giúp nút và text cao bằng nhau
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tên sách
            Text(book.title, modifier = Modifier.weight(1f)) // Dùng weight để tên sách dài tự xuống dòng

            Spacer(modifier = Modifier.width(16.dp))

            // Trạng thái mượn (Nút hoặc Text)
            when {
                // TRƯỜNG HỢP 1: SV hiện tại đã mượn
                isBorrowedByCurrent -> {
                    Text(
                        "✓ Đã mượn",
                        color = MaterialTheme.colorScheme.primary, // Màu xanh
                        fontWeight = FontWeight.Bold
                    )
                }
                // TRƯỜNG HỢP 2: Sách có sẵn để mượn
                isAvailable -> {
                    Button(onClick = onBorrowClick) {
                        Text("Mượn")
                    }
                }
                // TRƯỜNG HỢP 3: Sách đã bị SV khác mượn
                else -> {
                    Text(
                        "Đã có SV mượn",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) // Màu xám mờ
                    )
                }
            }
        }
    }
}

// --- 7. Composable cho Thanh điều hướng dưới (Không đổi) ---
@Composable
fun AppBottomNavigation() {
    // ... (Code giữ nguyên) ...
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


// --- 8. Composable MỚI cho Dialog Thêm Sinh viên (Không đổi) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    // ... (Code giữ nguyên) ...
    var studentName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Sinh viên mới") },
        text = {
            OutlinedTextField(
                value = studentName,
                onValueChange = { studentName = it },
                label = { Text("Tên sinh viên") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (studentName.isNotBlank()) {
                        onAdd(studentName)
                    }
                },
                enabled = studentName.isNotBlank()
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}