package com.example.smarttask4.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smarttask4.data.model.TaskResponse
import com.example.smarttask4.data.network.TaskRetrofitClient
import com.example.smarttask4.data.room.TaskEntity
import com.example.smarttask4.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

// ==================== 1. MÀN HÌNH HOME (ROOM DB + CHECKBOX) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: TaskViewModel) {
    // Lấy dữ liệu từ Room Database
    val taskList by viewModel.allTasks.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0F2F1)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Home, "Logo", tint = Color(0xFF00695C), modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("UTH SmartTasks", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00695C))
                        Text("Offline Mode (Room)", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Icon(Icons.Default.Notifications, "Noti", tint = Color(0xFFFFC107), modifier = Modifier.size(28.dp))
            }
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White, tonalElevation = 8.dp) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    FooterIcon(Icons.Default.Home, selectedTab == 0) { selectedTab = 0 }
                    FooterIcon(Icons.Default.DateRange, selectedTab == 1) { selectedTab = 1 }
                    Spacer(modifier = Modifier.width(48.dp))
                    FooterIcon(Icons.AutoMirrored.Filled.List, selectedTab == 2) { selectedTab = 2 }
                    FooterIcon(Icons.Default.Settings, selectedTab == 3) { selectedTab = 3 }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") },
                containerColor = Color(0xFF2196F3),
                shape = CircleShape,
                modifier = Modifier.size(60.dp).offset(y = 40.dp)
            ) {
                Icon(Icons.Default.Add, "Add", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(Color.White)) {
            if (taskList.isEmpty()) {
                EmptyView()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(taskList) { task ->
                        // Gọi Card mới có chức năng Checkbox & Delete
                        TaskItemCardDB(
                            task = task,
                            onDelete = { viewModel.deleteTask(task) },
                            onCheckedChange = { isChecked ->
                                // Cập nhật trạng thái vào Database
                                viewModel.onTaskChecked(task, isChecked)
                            },
                            onClick = {
                                // Chuyển sang trang chi tiết (vẫn dùng ID để gọi API demo)
                                navController.navigate("detail/${task.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// Card hiển thị Task từ Database (Có Checkbox hoạt động)
@Composable
fun TaskItemCardDB(
    task: TaskEntity,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val cardColor = try { Color(android.graphics.Color.parseColor(task.colorCode)) } catch (e: Exception) { Color(0xFFE3F2FD) }

    // Nếu xong thì màu xám, chưa xong thì màu ngẫu nhiên
    val backgroundColor = if (task.isCompleted) Color(0xFFEEEEEE) else cardColor
    val contentColor = if (task.isCompleted) Color.Gray else Color.Black

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // CHECKBOX THẬT
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.DarkGray,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = contentColor,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    fontSize = 13.sp,
                    color = contentColor.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Nút xóa
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

// ==================== 2. MÀN HÌNH DETAIL (GIỮ NGUYÊN API CŨ) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, taskId: String) {
    val sampleTask = TaskResponse(
        id = taskId,
        title = "Complete Android Project",
        description = "Finish the UI, integrate API, and write documentation",
        status = "In Progress",
        priority = "High",
        category = "Work",
        colorCode = "#FFCDD2"
    )

    var task by remember { mutableStateOf(sampleTask) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(taskId) {
        try {
            val result = TaskRetrofitClient.api.getTaskDetail(taskId)
            if (!result.title.isNullOrEmpty()) task = result
        } catch (e: Exception) { }
    }

    fun deleteTask() {
        coroutineScope.launch {
            try {
                TaskRetrofitClient.api.deleteTask(taskId)
                Toast.makeText(context, "Deleted (API)!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Deleted (Demo)!", Toast.LENGTH_SHORT).show()
            }
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail", fontWeight = FontWeight.Bold, color = Color(0xFF2196F3), fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.background(Color(0xFF2196F3), RoundedCornerShape(10.dp)).padding(8.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { deleteTask() }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.White, modifier = Modifier.background(Color(0xFFE65100), CircleShape).padding(8.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(20.dp).fillMaxSize().background(Color.White)) {
            Text(task.title ?: "Title", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(task.description ?: "Description", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEAB8B8)), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(80.dp)) {
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    StatItem(Icons.Default.Menu, "Category", task.category ?: "Work")
                    StatItem(Icons.AutoMirrored.Filled.List, "Status", task.status ?: "In Progress")
                    StatItem(Icons.Default.Star, "Priority", task.priority ?: "High")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Subtasks", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            SubtaskBox("This task is related to Fitness. It needs to be completed")
            SubtaskBox("Needs to be completed")

            Spacer(modifier = Modifier.height(24.dp))
            Text("Attachments", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Call, null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("document_1_0.pdf", color = Color.DarkGray)
                }
            }
        }
    }
}

// ==================== CÁC COMPONENT PHỤ ====================

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.DateRange, null, modifier = Modifier.size(80.dp), tint = Color.Gray)
        Text("No Tasks Yet!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Press + to add new task", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun FooterIcon(icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(icon, null, tint = if (isSelected) Color(0xFF2196F3) else Color.Gray, modifier = Modifier.size(28.dp))
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, modifier = Modifier.size(20.dp))
        Text(label, fontSize = 10.sp)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SubtaskBox(text: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(24.dp).border(2.dp, Color.Black, RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}