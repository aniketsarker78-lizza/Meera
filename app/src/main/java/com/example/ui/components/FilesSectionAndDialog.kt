package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LogEntry
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.PastelPurpleTertiary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted

@Composable
fun FilesSection(
  logs: List<LogEntry>,
  isPatternEnabled: Boolean,
  onRunSetupBat: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedFile by remember { mutableStateOf<String?>(null) }

  GlassmorphismCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 16.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Folder, contentDescription = null, tint = PastelPurpleTertiary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(6.dp))
        Text(
          text = "system_files.dir",
          color = PastelPurpleTertiary,
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 4 files: setup.bat, pattern_lock, memory_backup, log.txt
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val fileList = listOf(
          Triple("setup.bat", Icons.Default.Code, PastelPinkPrimary),
          Triple("pattern_lock", Icons.Default.Lock, PastelBlueSecondary),
          Triple("memory_backup", Icons.Default.Save, PastelPurpleTertiary),
          Triple("log.txt", Icons.Default.Description, CyberCyanNeon)
        )

        for ((fileName, icon, color) in fileList) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0x221B122C))
              .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .clickable { selectedFile = fileName }
              .padding(vertical = 10.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(icon, contentDescription = fileName, tint = color, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = fileName,
                color = TextLightPrimary,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }

  // File Viewer Dialog
  selectedFile?.let { file ->
    AlertDialog(
      onDismissRequest = { selectedFile = null },
      containerColor = Color(0xFF140D22),
      title = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = file, color = PastelPinkPrimary, fontSize = 16.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
          IconButton(onClick = { selectedFile = null }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
          }
        }
      },
      text = {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFF090510), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          when (file) {
            "setup.bat" -> {
              Column {
                Text(
                  text = """
@echo off
REM === MEERA ASSISTANT SETUP KERNEL ===
echo Initializing GboardFix Unicode Parser... [OK]
echo Initializing MFCC 32-dim Audio Feature Bank... [OK]
echo Setting Bengali TTS Locale to bn_BD (pitch=1.25, rate=0.95)... [OK]
echo Registering MeeraAccessibilityService... [ACTIVE]
echo Registering MeeraNotificationListener... [READY]
echo All systems operational for Aniket ♡
                  """.trimIndent(),
                  color = StatusGreen,
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = { onRunSetupBat(); selectedFile = null },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text("Execute setup.bat", color = CyberCyanNeon, fontSize = 12.sp)
                }
              }
            }
            "pattern_lock" -> {
              Column {
                Text(
                  text = """
# MEERA SECURITY CONFIGURATION
PATTERN_LOCK_STATUS=${if (isPatternEnabled) "ENABLED" else "DISABLED"}
BIOMETRIC_AUTH_ALLOWED=true
OWNER_VOICE_VERIFICATION=COSINE_0.82
TARGET_OWNER=Aniket
EMERGENCY_OVERRIDE=ACTIVE (Standard System Lock)
                  """.trimIndent(),
                  color = PastelBlueSecondary,
                  fontSize = 12.sp,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
            "memory_backup" -> {
              Column {
                Text(
                  text = """
{
  "system": "Meera Assistant v2.4",
  "owner": "Aniket",
  "personality": "Sweet 18yo Sanatani Bengali Girl",
  "language": "Bangla (bn_BD)",
  "values": ["Bhagavad Gita", "Vedic Peace", "Devotion"],
  "hobbies": ["Japanese Language Academy", "Helping Aniket", "Music"],
  "last_backup": "2026-11-11 11:37:15 UTC",
  "integrity_hash": "SHA256:7a9f8c2b1e4d"
}
                  """.trimIndent(),
                  color = PastelPurpleTertiary,
                  fontSize = 11.sp,
                  fontFamily = FontFamily.Monospace
                )
              }
            }
            "log.txt" -> {
              LazyColumn {
                items(logs) { log ->
                  Text(
                    text = "[${log.timestamp}] [${log.tag}] ${log.message}",
                    color = if (log.isError) StatusRed else TextLightSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { selectedFile = null }) {
          Text("Close", color = PastelPinkPrimary)
        }
      }
    )
  }
}

@Composable
fun FooterSection(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "made for aniket ",
        color = TextLightSecondary,
        fontSize = 13.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium
      )
      Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = "Love",
        tint = CyberPinkNeon,
        modifier = Modifier.size(15.dp)
      )
      Spacer(modifier = Modifier.size(4.dp))
      Text(
        text = "♡",
        color = CyberPinkNeon,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
