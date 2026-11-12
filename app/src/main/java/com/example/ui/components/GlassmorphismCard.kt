package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberGlassBorder
import com.example.ui.theme.CyberGlassBorderBlue
import com.example.ui.theme.CyberSurfaceDark

@Composable
fun GlassmorphismCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 16.dp,
  borderColor: Color = CyberGlassBorder,
  backgroundColor: Color = CyberSurfaceDark.copy(alpha = 0.78f),
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  val borderBrush = Brush.linearGradient(
    colors = listOf(
      borderColor,
      CyberGlassBorderBlue.copy(alpha = 0.35f),
      borderColor.copy(alpha = 0.6f)
    )
  )

  Box(
    modifier = modifier
      .clip(shape)
      .background(backgroundColor)
      .border(BorderStroke(1.2.dp, borderBrush), shape)
      .padding(14.dp),
    content = content
  )
}
