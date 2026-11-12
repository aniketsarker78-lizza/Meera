package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.CyberPurpleNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.PastelPurpleTertiary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary

@Composable
fun Y2kOrbVisualizer(
  isRunning: Boolean,
  statusText: String,
  onOrbClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "OrbPulse")

  val pulseScale by infiniteTransition.animateFloat(
    initialValue = if (isRunning) 0.85f else 0.95f,
    targetValue = if (isRunning) 1.15f else 1.02f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isRunning) 1200 else 2400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "Scale"
  )

  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isRunning) 5000 else 12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "Rotation"
  )

  GlassmorphismCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 18.dp
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Y2K Window Title Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0x33FFFFFF), RoundedCornerShape(8.dp))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(if (isRunning) StatusGreen else StatusRed)
          )
          Spacer(modifier = Modifier.size(6.dp))
          Text(
            text = "meera.exe [v2.4] ${if (isRunning) "[ONLINE]" else "[STANDBY]"}",
            color = TextLightPrimary,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        // Retro Window buttons
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("[-]", color = PastelBlueSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          Text("[□]", color = PastelPinkPrimary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
          Text("[✕]", color = StatusRed, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // The Pulsing Y2K Cyber Orb
      Box(
        modifier = Modifier
          .size(170.dp)
          .testTag("meera_orb")
          .clickable { onOrbClick() },
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(160.dp)) {
          val center = Offset(size.width / 2f, size.height / 2f)
          val baseRadius = size.width * 0.36f * pulseScale

          // Outer glowing ripple wave 1
          if (isRunning) {
            drawCircle(
              brush = Brush.radialGradient(
                colors = listOf(CyberPinkNeon.copy(alpha = 0.25f), Color.Transparent),
                center = center,
                radius = baseRadius * 1.5f
              ),
              center = center,
              radius = baseRadius * 1.45f
            )

            // Orbital cyber ring
            drawCircle(
              brush = Brush.sweepGradient(
                colors = listOf(CyberPinkNeon, CyberCyanNeon, CyberPurpleNeon, CyberPinkNeon),
                center = center
              ),
              center = center,
              radius = baseRadius * 1.25f,
              style = Stroke(width = 2.dp.toPx())
            )
          }

          // Main Glowing Orb Sphere
          drawCircle(
            brush = Brush.radialGradient(
              colors = if (isRunning) {
                listOf(
                  Color.White,
                  PastelPinkPrimary,
                  CyberPurpleNeon.copy(alpha = 0.85f),
                  CyberCyanNeon.copy(alpha = 0.4f)
                )
              } else {
                listOf(
                  Color(0xFFE2D4F0),
                  PastelPurpleTertiary.copy(alpha = 0.6f),
                  Color(0xFF1E1530)
                )
              },
              center = Offset(center.x - baseRadius * 0.25f, center.y - baseRadius * 0.25f),
              radius = baseRadius
            ),
            center = center,
            radius = baseRadius
          )

          // Sanatani Central Sacred Bindi (লাল টিপ) Sparkle
          drawCircle(
            color = Color(0xFFFF2255),
            center = center,
            radius = 5.dp.toPx()
          )
          drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            center = Offset(center.x - 1.5f, center.y - 1.5f),
            radius = 1.8.dp.toPx()
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status caption
      Text(
        text = statusText,
        color = if (isRunning) CyberCyanNeon else TextLightSecondary,
        fontSize = 13.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium
      )
    }
  }
}
