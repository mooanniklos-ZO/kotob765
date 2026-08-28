package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.data.UserPreferencesManager
import com.example.ui.BookAppMainScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val userPrefs = UserPreferencesManager(applicationContext)
    setContent {
      BookAppMainScreen(
        userPrefs = userPrefs,
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}

