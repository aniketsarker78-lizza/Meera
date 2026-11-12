package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassmorphismCard
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.CyberPinkNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.viewmodel.MeeraViewModel

@Composable
fun VoiceChatScreen(
  viewModel: MeeraViewModel,
  modifier: Modifier = Modifier
) {
  val voiceEnrollment by viewModel.voiceEnrollment.collectAsState()
  val chatMessages by viewModel.chatMessages.collectAsState()
  val isSpeaking by viewModel.ttsService.isSpeaking.collectAsState()

  var inputText by remember { mutableStateOf("") }
  var testPhraseInput by remember { mutableStateOf("Hey Meera") }
  var verificationTestResult by remember { mutableStateOf<String?>(null) }

  val quickPrompts = listOf(
    "তুমি দেখতে কেমন?",
    "একটি গীতার শ্লোক শোনাও",
    "জাপানি ক্লাসের কুইজ দাও",
    "ওয়াইফাই সেটিংস খোলো",
    "তুমি কে?"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // 1. Owner Voice Verification Card
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
            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = CyberPinkNeon, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(
              text = "Owner Voice Profile [MFCC > 0.82]",
              color = TextLightPrimary,
              fontSize = 12.5.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )
          }

          IconButton(onClick = { viewModel.resetVoiceEnrollment() }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 3 sample boxes
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (i in 1..3) {
            val isRecorded = voiceEnrollment.samplesCount >= i
            Box(
              modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .background(
                  if (isRecorded) Color(0xFF1B4332) else Color(0x221B122C),
                  RoundedCornerShape(8.dp)
                )
                .border(
                  1.dp,
                  if (isRecorded) StatusGreen else Color(0x44FFFFFF),
                  RoundedCornerShape(8.dp)
                ),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                if (isRecorded) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.size(4.dp))
                }
                Text(
                  text = "Sample $i",
                  color = if (isRecorded) StatusGreen else TextMuted,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = voiceEnrollment.statusText,
            color = if (voiceEnrollment.isEnrolled) StatusGreen else PastelPinkPrimary,
            fontSize = 11.sp
          )

          if (!voiceEnrollment.isEnrolled) {
            Button(
              onClick = { viewModel.enrollVoiceSample() },
              modifier = Modifier
                .height(34.dp)
                .testTag("record_sample_button"),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A102D)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.Mic, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("Record 'Hey Meera'", color = PastelPinkPrimary, fontSize = 11.sp)
            }
          }
        }

        // Test phrase evaluation test
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = testPhraseInput,
            onValueChange = { testPhraseInput = it },
            placeholder = { Text("Test regex & voice...", fontSize = 10.sp, color = TextMuted) },
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .height(46.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyberCyanNeon,
              unfocusedBorderColor = Color(0x33FFFFFF),
              focusedTextColor = TextLightPrimary,
              unfocusedTextColor = TextLightSecondary
            )
          )

          Spacer(modifier = Modifier.width(6.dp))

          Button(
            onClick = {
              val verified = viewModel.voiceVerifier.verifySpeaker(testPhraseInput)
              verificationTestResult = if (verified) {
                "Matched! (Regex Valid + Cosine > 0.82) ✓"
              } else {
                "Rejected! (Pattern failed or false trigger word) ✗"
              }
            },
            modifier = Modifier.height(46.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Verify", color = CyberCyanNeon, fontSize = 11.sp)
          }
        }

        verificationTestResult?.let { res ->
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = res,
            color = if (res.contains("Matched")) StatusGreen else StatusRed,
            fontSize = 10.5.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Audio Speech indicator
    AnimatedVisibility(visible = isSpeaking) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0x33FF4D94), RoundedCornerShape(8.dp))
          .border(1.dp, CyberPinkNeon, RoundedCornerShape(8.dp))
          .padding(vertical = 6.dp, horizontal = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.VolumeUp, contentDescription = null, tint = CyberPinkNeon, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.size(6.dp))
          Text(
            text = "মীরা মিষ্টি বাংলায় কথা বলছে... (Pitch 1.25, Rate 0.95)",
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // Quick Prompts Row
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(quickPrompts) { prompt ->
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x28FFFFFF))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
            .clickable {
              viewModel.sendChatMessage(prompt)
            }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(prompt, color = PastelPinkPrimary, fontSize = 11.sp)
        }
      }
    }

    // Chat Message Log
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .background(Color(0x181B122C), RoundedCornerShape(12.dp))
        .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
        .padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      reverseLayout = false
    ) {
      items(chatMessages) { (text, isUser) ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(if (isUser) Color(0xFF23395B) else Color(0xFF4A102D))
              .border(
                1.dp,
                if (isUser) CyberCyanNeon.copy(alpha = 0.5f) else PastelPinkPrimary.copy(alpha = 0.5f),
                RoundedCornerShape(14.dp)
              )
              .padding(horizontal = 12.dp, vertical = 8.dp)
          ) {
            Column {
              Text(
                text = if (isUser) "অনিকেত (Owner)" else "মীরা (Meera ♡)",
                color = if (isUser) PastelBlueSecondary else PastelPinkPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = text,
                color = TextLightPrimary,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }

    // Message Input Field (Gboard / Ridmik Unicode typing compatible)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        placeholder = { Text("বাংলায় কিছু লিখুন বা প্রশ্ন করুন...", color = TextMuted, fontSize = 12.sp) },
        modifier = Modifier
          .weight(1f)
          .testTag("chat_text_input"),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {
          if (inputText.isNotBlank()) {
            viewModel.sendChatMessage(inputText)
            inputText = ""
          }
        }),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = PastelPinkPrimary,
          unfocusedBorderColor = Color(0x44FFFFFF),
          focusedTextColor = TextLightPrimary,
          unfocusedTextColor = TextLightSecondary
        )
      )

      Spacer(modifier = Modifier.width(8.dp))

      Button(
        onClick = {
          if (inputText.isNotBlank()) {
            viewModel.sendChatMessage(inputText)
            inputText = ""
          }
        },
        modifier = Modifier
          .size(50.dp)
          .testTag("send_chat_button"),
        colors = ButtonDefaults.buttonColors(containerColor = CyberPinkNeon),
        shape = CircleShape,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
      ) {
        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
      }
    }
  }
}
