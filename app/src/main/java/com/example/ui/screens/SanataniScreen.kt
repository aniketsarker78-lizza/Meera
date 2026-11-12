package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.data.SanataniKnowledgeBase
import com.example.ui.components.GlassmorphismCard
import com.example.ui.theme.CyberCyanNeon
import com.example.ui.theme.PastelBlueSecondary
import com.example.ui.theme.PastelPinkPrimary
import com.example.ui.theme.PastelPurpleTertiary
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextLightSecondary
import com.example.ui.theme.TextMuted
import com.example.viewmodel.MeeraViewModel

@Composable
fun SanataniScreen(
  viewModel: MeeraViewModel,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Banner
    item {
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "ॐ তদ্বিষ্ণোঃ পরমং পদম্ ॐ",
            color = StatusAmber,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "মীরার সনাতনী জ্ঞানকোষ ও গীতা দর্শন",
            color = PastelPinkPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "১৮ বছর বয়সী সনাতনী বাঙালি মেয়ে মীরার কণ্ঠে বেদ, উপনিষদ ও শ্রীমদ্ভগবদ্গীতার অমৃতবাণী।",
            color = TextLightSecondary,
            fontSize = 11.5.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    // Daily Gita Verse Section
    item {
      Text(
        text = "শ্রীমদ্ভগবদ্গীতা শ্লোকাবলী (Bhagavad Gita)",
        color = PastelBlueSecondary,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
    }

    items(SanataniKnowledgeBase.gitaVerses) { verse ->
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 14.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "অধ্যায় ${verse.chapter}, শ্লোক ${verse.verse}",
              color = StatusAmber,
              fontSize = 12.sp,
              fontFamily = FontFamily.Monospace,
              fontWeight = FontWeight.Bold
            )

            Button(
              onClick = {
                viewModel.ttsService.speak("${verse.bengaliMeaning}")
                viewModel.logEvent("GITA", "Spoken verse Ch${verse.chapter}:${verse.verse}")
              },
              modifier = Modifier.height(32.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38234D)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = PastelPinkPrimary, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("শুনুন", color = PastelPinkPrimary, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Sanskrit Text Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color(0xFF0F0A1A), RoundedCornerShape(8.dp))
              .border(1.dp, Color(0x33FFA6C9), RoundedCornerShape(8.dp))
              .padding(10.dp)
          ) {
            Text(
              text = verse.sanskrit,
              color = Color(0xFFFFD1E6),
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              lineHeight = 18.sp
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "বাংলা অর্থ: ${verse.bengaliMeaning}",
            color = TextLightPrimary,
            fontSize = 12.sp,
            lineHeight = 17.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "প্রয়োগ: ${verse.theme}",
            color = PastelPurpleTertiary,
            fontSize = 10.5.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Vedic Mantras Section
    item {
      Text(
        text = "বৈদিক মহামন্ত্র (Vedic Sacred Mantras)",
        color = CyberCyanNeon,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
    }

    items(SanataniKnowledgeBase.vedicMantras) { mantra ->
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 14.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = mantra.name,
              color = PastelPinkPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )

            Button(
              onClick = {
                viewModel.ttsService.speak("${mantra.bengaliTransliteration}। এর অর্থ: ${mantra.bengaliMeaning}")
                viewModel.logEvent("MANTRA", "Spoken mantra: ${mantra.name}")
              },
              modifier = Modifier.height(30.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(Icons.Default.VolumeUp, contentDescription = "Play", tint = CyberCyanNeon, modifier = Modifier.size(13.dp))
              Spacer(modifier = Modifier.size(4.dp))
              Text("জপ শুনুন", color = CyberCyanNeon, fontSize = 10.5.sp)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = mantra.sanskrit,
            color = StatusAmber,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = mantra.bengaliMeaning,
            color = TextLightSecondary,
            fontSize = 11.5.sp
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "উপযোগিতা: ${mantra.timeOrOccasion}",
            color = TextMuted,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
          )
        }
      }
    }

    // Puja Vidhi Section
    item {
      Text(
        text = "পূজা বিধি ও বাঙালি উৎসব নির্দেশিকা",
        color = PastelPinkPrimary,
        fontSize = 14.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold
      )
    }

    items(SanataniKnowledgeBase.pujaGuides) { (title, description) ->
      GlassmorphismCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 14.dp
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(title, color = PastelPinkPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(description, color = TextLightSecondary, fontSize = 11.5.sp, lineHeight = 16.sp)
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
