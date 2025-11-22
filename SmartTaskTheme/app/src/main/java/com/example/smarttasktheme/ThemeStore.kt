package com.example.smarttasktheme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Tạo DataStore (Tên file lưu trữ là "settings")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeStore(private val context: Context) {

    // Khóa để lưu màu (giống như key trong Map)
    companion object {
        val THEME_COLOR_KEY = stringPreferencesKey("theme_color")
    }

    // 2. Hàm lấy màu đã lưu (Mặc định là Blue nếu chưa lưu gì)
    val themeFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_COLOR_KEY] ?: "Blue"
        }

    // 3. Hàm lưu màu mới
    suspend fun saveTheme(colorName: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_COLOR_KEY] = colorName
        }
    }
}