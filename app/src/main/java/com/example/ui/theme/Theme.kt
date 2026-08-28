package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ReadingThemeMode {
  PARCHMENT,
  SEPIA,
  EMERALD_DARK,
  MODERN_WHITE
}

private val DarkColorScheme = darkColorScheme(
  primary = GoldLight,
  onPrimary = OnGoldContainer,
  primaryContainer = EmeraldDark,
  onPrimaryContainer = GoldContainer,
  secondary = EmeraldLight,
  onSecondary = Color.White,
  secondaryContainer = DarkSurfaceVariant,
  onSecondaryContainer = DarkTextPrimary,
  tertiary = GoldPrimary,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = GoldDark
)

private val LightColorScheme = lightColorScheme(
  primary = EmeraldPrimary,
  onPrimary = Color.White,
  primaryContainer = EmeraldContainer,
  onPrimaryContainer = OnEmeraldContainer,
  secondary = GoldPrimary,
  onSecondary = Color.White,
  secondaryContainer = GoldContainer,
  onSecondaryContainer = OnGoldContainer,
  tertiary = AccentTeal,
  background = ParchmentLight,
  onBackground = Color(0xFF1E2421),
  surface = ParchmentSurface,
  onSurface = Color(0xFF1E2421),
  surfaceVariant = ParchmentBorder,
  onSurfaceVariant = Color(0xFF4A5550),
  outline = GoldPrimary
)

private val SepiaColorScheme = lightColorScheme(
  primary = Color(0xFF6B4226),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFDFCCB7),
  onPrimaryContainer = Color(0xFF2C1608),
  secondary = Color(0xFF8C5E35),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFEADBCE),
  onSecondaryContainer = Color(0xFF331C08),
  tertiary = Color(0xFF5C6B3D),
  background = SepiaBackground,
  onBackground = SepiaText,
  surface = SepiaSurface,
  onSurface = SepiaText,
  surfaceVariant = Color(0xFFD8CCB5),
  onSurfaceVariant = Color(0xFF524436),
  outline = Color(0xFFB5936D)
)

@Composable
fun MyApplicationTheme(
  themeMode: ReadingThemeMode = ReadingThemeMode.PARCHMENT,
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when (themeMode) {
    ReadingThemeMode.PARCHMENT -> LightColorScheme
    ReadingThemeMode.SEPIA -> SepiaColorScheme
    ReadingThemeMode.EMERALD_DARK -> DarkColorScheme
    ReadingThemeMode.MODERN_WHITE -> if (darkTheme) DarkColorScheme else LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

