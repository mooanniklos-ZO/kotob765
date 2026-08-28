package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.ui.theme.ReadingThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("book_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<ReadingThemeMode> = _themeMode.asStateFlow()

    private val _fontSize = MutableStateFlow(prefs.getInt("font_size", 18))
    val fontSize: StateFlow<Int> = _fontSize.asStateFlow()

    private val _is3DViewMode = MutableStateFlow(prefs.getBoolean("is_3d_mode", true))
    val is3DViewMode: StateFlow<Boolean> = _is3DViewMode.asStateFlow()

    private val _bookmarks = MutableStateFlow(loadBookmarks())
    val bookmarks: StateFlow<Set<String>> = _bookmarks.asStateFlow()

    private val _lastReadSectionId = MutableStateFlow(prefs.getString("last_section_id", "foreword") ?: "foreword")
    val lastReadSectionId: StateFlow<String> = _lastReadSectionId.asStateFlow()

    private fun loadThemeMode(): ReadingThemeMode {
        val name = prefs.getString("theme_mode", ReadingThemeMode.PARCHMENT.name)
        return try {
            ReadingThemeMode.valueOf(name ?: ReadingThemeMode.PARCHMENT.name)
        } catch (e: Exception) {
            ReadingThemeMode.PARCHMENT
        }
    }

    private fun loadBookmarks(): Set<String> {
        return prefs.getStringSet("bookmarks_set", emptySet()) ?: emptySet()
    }

    fun setThemeMode(mode: ReadingThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    fun setFontSize(size: Int) {
        val clamped = size.coerceIn(14, 30)
        prefs.edit().putInt("font_size", clamped).apply()
        _fontSize.value = clamped
    }

    fun set3DViewMode(enabled: Boolean) {
        prefs.edit().putBoolean("is_3d_mode", enabled).apply()
        _is3DViewMode.value = enabled
    }

    fun toggleBookmark(sectionId: String) {
        val current = _bookmarks.value.toMutableSet()
        if (current.contains(sectionId)) {
            current.remove(sectionId)
        } else {
            current.add(sectionId)
        }
        prefs.edit().putStringSet("bookmarks_set", current).apply()
        _bookmarks.value = current
    }

    fun saveLastReadSection(sectionId: String) {
        prefs.edit().putString("last_section_id", sectionId).apply()
        _lastReadSectionId.value = sectionId
    }

    fun saveNote(sectionId: String, note: String) {
        prefs.edit().putString("note_$sectionId", note).apply()
    }

    fun getNote(sectionId: String): String {
        return prefs.getString("note_$sectionId", "") ?: ""
    }
}
