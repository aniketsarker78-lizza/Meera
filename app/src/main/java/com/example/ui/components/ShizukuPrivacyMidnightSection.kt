package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted

@Composable
fun ShizukuPrivacyMidnightSection(
  isShizukuRunning: Boolean,
  onGrantAllPermissions: () -> Unit,
  onShellTestClick: () -> Unit,
  privacyGuard: Boolean,
  onTogglePrivacyGuard: () -> Unit,
  isExactAlarmGranted: Boolean,
  midnightWishStatus: String,
  onScheduleMidnightWish: () -> Unit,
  onRequestExactAlarm: () -> Unit,
  autopilotChat: Boolean,
  onToggleAutopilot: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // shizuku.sys Card
    GlassmorphismCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 16.dp
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Terminal, contentDescription = null, tint = PastelBlueSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "shizuku.sys",
              color = PastelBlueSecondary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = if (isShizukuRunning) "Running ✓" else "Standby (Normal Perms)",
            color = if (isShizukuRunning) StatusGreen else TextMuted,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "System Shell Bridge: Standard Android API permission orchestrator active.",
          color = TextLightSecondary,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onGrantAllPermissions,
            modifier = Modifier
              .weight(1.3f)
              .height(38.dp)
              .testTag("grant_all_perms_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF23395B)
            ),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberCyanNeon, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.size(4.dp))
            Text("Grant All Perms A-Z", color = CyberCyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = onShellTestClick,
            modifier = Modifier
              .weight(0.9f)
              .height(38.dp)
              .testTag("shell_test_id_button"),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x66FFFFFF)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Shell test id", color = TextLightPrimary, fontSize = 11.sp)
          }
        }
      }
    }

    // privacy.sys Card
    GlassmorphismCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 16.dp
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "privacy.sys",
              color = PastelPinkPrimary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (privacyGuard) "Privacy guard ON: Mic & Data Masked" else "Privacy guard OFF",
            color = if (privacyGuard) StatusGreen else TextMuted,
            fontSize = 11.sp
          )
        }

        Button(
          onClick = onTogglePrivacyGuard,
          modifier = Modifier
            .height(36.dp)
            .testTag("toggle_privacy_guard_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (privacyGuard) Color(0xFF1B4332) else Color(0xFF4A102D)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = if (privacyGuard) "Turn Guard OFF" else "Turn Guard ON",
            color = if (privacyGuard) StatusGreen else PastelPinkPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // midnight_wish Card
    GlassmorphismCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 16.dp
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Alarm, contentDescription = null, tint = PastelBlueSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "midnight_wish.alarm",
              color = PastelBlueSecondary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = if (isExactAlarmGranted) "Exact alarm ✓" else "Exact alarm ✗",
            color = if (isExactAlarmGranted) StatusGreen else StatusRed,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = midnightWishStatus,
          color = TextLightSecondary,
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onScheduleMidnightWish,
            modifier = Modifier
              .weight(1f)
              .height(36.dp)
              .testTag("schedule_midnight_wish_button"),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF3C1642)
            ),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Schedule 12:00 AM Wish", color = PastelPinkPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          if (!isExactAlarmGranted) {
            OutlinedButton(
              onClick = onRequestExactAlarm,
              modifier = Modifier.height(36.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("SCHEDULE_EXACT_ALARM", color = StatusRed, fontSize = 10.sp)
            }
          }
        }
      }
    }

    // autopilot.chat Card
    GlassmorphismCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 16.dp
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "autopilot.chat",
              color = PastelPinkPrimary,
              fontSize = 13.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = if (autopilotChat) "Autopilot ON: Smart contextual reply active" else "Autopilot OFF",
            color = if (autopilotChat) StatusGreen else TextMuted,
            fontSize = 11.sp
          )
        }

        Button(
          onClick = onToggleAutopilot,
          modifier = Modifier
            .height(36.dp)
            .testTag("toggle_autopilot_button"),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (autopilotChat) Color(0xFF1B4332) else Color(0xFF4A102D)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = if (autopilotChat) "Turn Autopilot OFF" else "Turn Autopilot ON",
            color = if (autopilotChat) StatusGreen else PastelPinkPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
