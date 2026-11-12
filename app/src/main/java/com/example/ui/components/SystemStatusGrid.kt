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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SystemStatusItem
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
fun SystemStatusGrid(
  items: List<SystemStatusItem>,
  geminiKey: String,
  isGeminiKeySaved: Boolean,
  onSaveGeminiKey: (String) -> Unit,
  isPatternEnabled: Boolean,
  onTogglePattern: () -> Unit,
  onItemActionClick: (SystemStatusItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var keyInput by remember(geminiKey) { mutableStateOf(geminiKey) }

  GlassmorphismCard(
    modifier = modifier.fillMaxWidth(),
    cornerRadius = 18.dp
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "system_status.sys [2-Column Matrix]",
          color = CyberCyanNeon,
          fontSize = 13.sp,
          fontFamily = FontFamily.Monospace,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "14/14 Modules",
          color = TextMuted,
          fontSize = 11.sp,
          fontFamily = FontFamily.Monospace
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 2-Column Grid Layout for 12 standard items
      val gridItems = items.filter { it.id != "gemini" && it.id != "pattern" }
      val rows = gridItems.chunked(2)

      for (row in rows) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (item in row) {
            Box(
              modifier = Modifier
                .weight(1f)
                .background(Color(0x221B122C), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                .clickable { onItemActionClick(item) }
                .padding(8.dp)
            ) {
              Column {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.name,
                    color = TextLightPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )

                  Icon(
                    imageVector = if (item.isGranted) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (item.isGranted) StatusGreen else StatusRed,
                    modifier = Modifier.size(15.dp)
                  )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                  text = item.permissionOrService,
                  color = TextMuted,
                  fontSize = 9.5.sp,
                  fontFamily = FontFamily.Monospace,
                  maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = if (item.isGranted) "Granted ✓" else item.actionHint,
                  color = if (item.isGranted) StatusGreen else PastelPinkPrimary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
          if (row.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Item 13: Gemini key EncryptedSharedPreferences with green ✓ when saved
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0x281B122C), RoundedCornerShape(12.dp))
          .border(1.dp, if (isGeminiKeySaved) StatusGreen.copy(alpha = 0.6f) else Color(0x33FFFFFF), RoundedCornerShape(12.dp))
          .padding(10.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Key, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.size(6.dp))
              Text(
                text = "Gemini Key (EncryptedSharedPreferences)",
                color = TextLightPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            if (isGeminiKeySaved) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Saved", tint = StatusGreen, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.size(4.dp))
                Text("Saved ✓", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = keyInput,
              onValueChange = { keyInput = it },
              modifier = Modifier
                .weight(1f)
                .testTag("gemini_key_input"),
              placeholder = { Text("Enter Gemini API Key...", color = TextMuted, fontSize = 11.sp) },
              visualTransformation = PasswordVisualTransformation(),
              singleLine = true,
              keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = { onSaveGeminiKey(keyInput) }),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PastelPinkPrimary,
                unfocusedBorderColor = Color(0x44FFFFFF),
                focusedTextColor = TextLightPrimary,
                unfocusedTextColor = TextLightSecondary
              )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
              onClick = { onSaveGeminiKey(keyInput) },
              modifier = Modifier
                .height(50.dp)
                .testTag("save_gemini_key_button"),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isGeminiKeySaved) Color(0xFF1B4332) else Color(0xFF4A102D)
              ),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Save, contentDescription = null, tint = if (isGeminiKeySaved) StatusGreen else PastelPinkPrimary, modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Item 14: Pattern Lock (OFF/ON)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0x281B122C), RoundedCornerShape(12.dp))
          .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
          .padding(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = PastelBlueSecondary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Column {
              Text(
                text = "Pattern Lock Security",
                color = TextLightPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = if (isPatternEnabled) "Pattern Active (Protected) ✓" else "Pattern OFF",
                color = if (isPatternEnabled) StatusGreen else TextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
              )
            }
          }

          Switch(
            checked = isPatternEnabled,
            onCheckedChange = { onTogglePattern() },
            modifier = Modifier.testTag("pattern_lock_switch"),
            colors = SwitchDefaults.colors(
              checkedThumbColor = PastelBlueSecondary,
              checkedTrackColor = Color(0x668AE0FF)
            )
          )
        }
      }
    }
  }
}
