package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WakeWordState
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted

@Composable
fun ControlPanelSection(
  isRunning: Boolean,
  onStartClick: () -> Unit,
  onStopClick: () -> Unit,
  onTestNowClick: () -> Unit,
  onLearnGeminiClick: () -> Unit,
  geminiScreenDefault: Boolean,
  onToggleGeminiScreenDefault: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  GlassmorphismCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 16.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = "control_panel.sys",
        color = PastelBlueSecondary,
        fontSize = 13.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(12.dp))

      // START / STOP Meera buttons row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // START Meera (Cyber Blue)
        Button(
          onClick = onStartClick,
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("start_meera_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
          ),
          shape = RoundedCornerShape(12.dp),
          contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .background(
                Brush.horizontalGradient(listOf(Color(0xFF00B4D8), Color(0xFF0077B6))),
                RoundedCornerShape(12.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = Color.White)
              Spacer(modifier = Modifier.size(6.dp))
              Text("START Meera", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        }

        // STOP Meera
        OutlinedButton(
          onClick = onStopClick,
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("stop_meera_button"),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0x22FF4E6D)
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.8f)),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Stop, contentDescription = "Stop", tint = StatusRed)
            Spacer(modifier = Modifier.size(6.dp))
            Text("STOP Meera", color = StatusRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Test now (without Hey Google)
      Button(
        onClick = onTestNowClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("test_now_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0x33FFA6C9)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, PastelPinkPrimary.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.VolumeUp, contentDescription = "Test Voice", tint = PastelPinkPrimary)
          Spacer(modifier = Modifier.size(8.dp))
          Text(
            "Test now (without Hey Google)",
            color = TextLightPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Learn Gemini screen & Gemini screen default toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onLearnGeminiClick() }
            .padding(vertical = 4.dp, horizontal = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.School, contentDescription = "Learn", tint = PastelBlueSecondary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.size(6.dp))
          Text("Learn Gemini screen", color = PastelBlueSecondary, fontSize = 12.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text("Gemini screen default", color = TextLightSecondary, fontSize = 11.sp)
          Spacer(modifier = Modifier.size(6.dp))
          Switch(
            checked = geminiScreenDefault,
            onCheckedChange = onToggleGeminiScreenDefault,
            modifier = Modifier.testTag("gemini_screen_default_switch"),
            colors = SwitchDefaults.colors(
              checkedThumbColor = PastelPinkPrimary,
              checkedTrackColor = Color(0x66FF94C2)
            )
          )
        }
      }
    }
  }
}

@Composable
fun WakeWordsSection(
  wakeState: WakeWordState,
  onToggleHeyGoogle: (Boolean) -> Unit,
  onToggleHeyMeera: (Boolean) -> Unit,
  onDownloadModelClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  GlassmorphismCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 16.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .animateContentSize()
    ) {
      Text(
        text = "wake_words.cfg",
        color = PastelPinkPrimary,
        fontSize = 13.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Hey Google via Gemini screen default ON pink
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Hey Google via Gemini screen",
            color = TextLightPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = "default ON (pink)",
            color = PastelPinkPrimary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Switch(
          checked = wakeState.heyGoogleGemini,
          onCheckedChange = onToggleHeyGoogle,
          modifier = Modifier.testTag("hey_google_switch"),
          colors = SwitchDefaults.colors(
            checkedThumbColor = CyberPinkNeon,
            checkedTrackColor = Color(0xFFFF94C2)
          )
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Hey Meera far-field default OFF
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Hey Meera far-field",
            color = TextLightPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = "default OFF (acoustic trigger)",
            color = TextMuted,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Switch(
          checked = wakeState.heyMeeraFarField,
          onCheckedChange = onToggleHeyMeera,
          modifier = Modifier.testTag("hey_meera_switch"),
          colors = SwitchDefaults.colors(
            checkedThumbColor = PastelBlueSecondary,
            checkedTrackColor = Color(0x558AE0FF)
          )
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Model download status & action
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0x221B122C), RoundedCornerShape(10.dp))
          .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
          .padding(10.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                Icons.Default.Hearing,
                contentDescription = null,
                tint = if (wakeState.isModelDownloaded) StatusGreen else StatusRed,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.size(6.dp))
              Text(
                text = if (wakeState.isModelDownloaded) "Hey Meera model: Ready ✓" else "Hey Meera model: Not downloaded",
                color = if (wakeState.isModelDownloaded) StatusGreen else StatusRed,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }

            if (!wakeState.isModelDownloaded) {
              Button(
                onClick = onDownloadModelClick,
                enabled = !wakeState.isDownloading,
                modifier = Modifier
                  .height(36.dp)
                  .testTag("download_model_button"),
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF4A102D)
                ),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                  text = if (wakeState.isDownloading) "Downloading..." else "Download 40MB",
                  color = PastelPinkPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          if (wakeState.isDownloading) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              progress = { wakeState.downloadProgress },
              modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
              color = CyberPinkNeon,
              trackColor = Color(0x33FFFFFF)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "openWakeWord / Vosk 40MB: ${(wakeState.downloadProgress * 100).toInt()}%",
              color = TextMuted,
              fontSize = 10.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      }
    }
  }
}
