package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Y2kColorScheme = darkColorScheme(
  primary = PastelPinkPrimary,
  onPrimary = Color(0xFF2E0018),
  primaryContainer = Color(0xFF4A102D),
  onPrimaryContainer = PastelPinkLight,

  secondary = PastelBlueSecondary,
  onSecondary = Color(0xFF003548),
  secondaryContainer = Color(0xFF0D3F54),
  onSecondaryContainer = PastelBlueLight,

  tertiary = PastelPurpleTertiary,
  onTertiary = Color(0xFF280052),
  tertiaryContainer = Color(0xFF421C75),
  onTertiaryContainer = PastelLavenderLight,

  background = CyberDarkBg,
  onBackground = TextLightPrimary,

  surface = CyberSurfaceDark,
  onSurface = TextLightPrimary,
  surfaceVariant = Color(0xFF26193E),
  onSurfaceVariant = TextLightSecondary,

  outline = CyberGlassBorder,
  outlineVariant = CyberGlassBorderBlue
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = Y2kColorScheme,
    typography = Typography,
    content = content
  )
}
