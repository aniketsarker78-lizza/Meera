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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.JapaneseTeacherModule
import com.example.ui.components.GlassmorphismCard
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
import com.example.viewmodel.MeeraViewModel

@Composable
fun JapaneseScreen(
  viewModel: MeeraViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("হিরাগানা", "কাতাকানা", "N5-N1 শব্দভাণ্ডার", "কুইজ পরীক্ষা")

  // Quiz State
  var currentQuestionIndex by remember { mutableIntStateOf(0) }
  var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
  var isSubmitted by remember { mutableStateOf(false) }
  var score by remember { mutableIntStateOf(0) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(14.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Translate, contentDescription = null, tint = PastelBlueSecondary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.size(8.dp))
        Text(
          text = "মীরার জাপানি একাডেমি (日本語)",
          color = TextLightPrimary,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Text("N5 - N1", color = PastelPinkPrimary, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Navigation Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color(0x281B122C),
      contentColor = PastelPinkPrimary,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
          modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = CyberPinkNeon
        )
      }
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              color = if (selectedTab == index) PastelPinkPrimary else TextMuted,
              fontSize = 11.5.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Content based on tab
    when (selectedTab) {
      0 -> {
        // Hiragana
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            Text(
              "হিরাগানা চার্ট (Hiragana - মৌলিক জাপানি বর্ণমালা)",
              color = PastelPinkPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
          items(JapaneseTeacherModule.hiraganaList) { item ->
            GlassmorphismCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .background(Color(0xFF3B1E4A), RoundedCornerShape(8.dp))
                      .border(1.dp, PastelPinkPrimary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(item.kana, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                  }

                  Spacer(modifier = Modifier.size(10.dp))

                  Column {
                    Text(
                      text = "রোমাজি: ${item.romaji}  |  বাংলা উচ্চারণ: ${item.bengaliPronunciation}",
                      color = TextLightPrimary,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                    Text(
                      text = "উদাহরণ: ${item.sampleWord} → ${item.sampleMeaningBengali}",
                      color = TextLightSecondary,
                      fontSize = 11.sp
                    )
                  }
                }

                IconButton(
                  onClick = {
                    viewModel.ttsService.speak("${item.romaji}। উদাহরণ: ${item.sampleMeaningBengali}")
                  }
                ) {
                  Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = PastelPinkPrimary)
                }
              }
            }
          }
        }
      }

      1 -> {
        // Katakana
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            Text(
              "কাতাকানা চার্ট (Katakana - বিদেশি শব্দের বর্ণমালা)",
              color = PastelBlueSecondary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
          items(JapaneseTeacherModule.katakanaList) { item ->
            GlassmorphismCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .background(Color(0xFF1E3A5F), RoundedCornerShape(8.dp))
                      .border(1.dp, CyberCyanNeon, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(item.kana, color = CyberCyanNeon, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                  }

                  Spacer(modifier = Modifier.size(10.dp))

                  Column {
                    Text(
                      text = "রোমাজি: ${item.romaji}  |  বাংলা উচ্চারণ: ${item.bengaliPronunciation}",
                      color = TextLightPrimary,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                    Text(
                      text = "উদাহরণ: ${item.sampleWord} → ${item.sampleMeaningBengali}",
                      color = TextLightSecondary,
                      fontSize = 11.sp
                    )
                  }
                }

                IconButton(
                  onClick = {
                    viewModel.ttsService.speak("${item.romaji}। উদাহরণ: ${item.sampleMeaningBengali}")
                  }
                ) {
                  Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = CyberCyanNeon)
                }
              }
            }
          }
        }
      }

      2 -> {
        // Vocabulary N5
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          item {
            Text(
              "JLPT স্তর ও অপরিহার্য শব্দার্থ (Essential Vocabulary)",
              color = PastelPurpleTertiary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          items(JapaneseTeacherModule.n5Vocabulary) { (kana, romaji, meaning) ->
            GlassmorphismCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 10.dp
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(text = kana, color = PastelPinkPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                  Text(text = romaji, color = PastelBlueSecondary, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                  Text(text = "বাংলা: $meaning", color = TextLightPrimary, fontSize = 12.sp)
                }

                Button(
                  onClick = { viewModel.ttsService.speak(meaning) },
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38204D)),
                  shape = RoundedCornerShape(6.dp),
                  modifier = Modifier.height(32.dp)
                ) {
                  Icon(Icons.Default.VolumeUp, contentDescription = null, tint = PastelPinkPrimary, modifier = Modifier.size(13.dp))
                  Spacer(modifier = Modifier.size(4.dp))
                  Text("উচ্চারণ", color = PastelPinkPrimary, fontSize = 10.5.sp)
                }
              }
            }
          }
        }
      }

      3 -> {
        // Quiz Section
        val currentQuestion = JapaneseTeacherModule.sampleQuizQuestions[currentQuestionIndex]

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
              Text(
                text = "প্রশ্ন ${currentQuestionIndex + 1} / ${JapaneseTeacherModule.sampleQuizQuestions.size}",
                color = PastelBlueSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )

              Text(
                text = "স্কোর: $score",
                color = StatusGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = currentQuestion.prompt,
              color = TextLightPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            currentQuestion.options.forEachIndexed { optIndex, optionText ->
              val isSelected = selectedOptionIndex == optIndex
              val isCorrect = optIndex == currentQuestion.correctIndex

              val backgroundColor = when {
                !isSubmitted && isSelected -> Color(0xFF4A102D)
                isSubmitted && isCorrect -> Color(0xFF1B4332)
                isSubmitted && isSelected && !isCorrect -> Color(0xFF4D141E)
                else -> Color(0x221B122C)
              }

              val borderColor = when {
                !isSubmitted && isSelected -> PastelPinkPrimary
                isSubmitted && isCorrect -> StatusGreen
                isSubmitted && isSelected && !isCorrect -> StatusRed
                else -> Color(0x33FFFFFF)
              }

              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(backgroundColor)
                  .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                  .clickable(enabled = !isSubmitted) {
                    selectedOptionIndex = optIndex
                  }
                  .padding(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "${optIndex + 1}. $optionText",
                    color = TextLightPrimary,
                    fontSize = 13.sp
                  )

                  if (isSubmitted) {
                    if (isCorrect) {
                      Icon(Icons.Default.Check, contentDescription = null, tint = StatusGreen)
                    } else if (isSelected) {
                      Icon(Icons.Default.Close, contentDescription = null, tint = StatusRed)
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isSubmitted) {
              Button(
                onClick = {
                  if (selectedOptionIndex != null) {
                    isSubmitted = true
                    val isCorrect = selectedOptionIndex == currentQuestion.correctIndex
                    if (isCorrect) score += 10
                    viewModel.ttsService.speak(
                      if (isCorrect) "একদম সঠিক! ${currentQuestion.explanationBengali}"
                      else "উত্তরটি ভুল হয়েছে। সঠিক উত্তরটি জেনে নাও: ${currentQuestion.explanationBengali}"
                    )
                  }
                },
                enabled = selectedOptionIndex != null,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyberPinkNeon),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("উত্তর যাচাই করুন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            } else {
              Column {
                Text(
                  text = currentQuestion.explanationBengali,
                  color = if (selectedOptionIndex == currentQuestion.correctIndex) StatusGreen else StatusRed,
                  fontSize = 11.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                  onClick = {
                    if (currentQuestionIndex < JapaneseTeacherModule.sampleQuizQuestions.size - 1) {
                      currentQuestionIndex++
                      selectedOptionIndex = null
                      isSubmitted = false
                    } else {
                      // Restart
                      currentQuestionIndex = 0
                      selectedOptionIndex = null
                      isSubmitted = false
                      score = 0
                    }
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23395B)),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    if (currentQuestionIndex < JapaneseTeacherModule.sampleQuizQuestions.size - 1) "পরবর্তী প্রশ্ন" else "কুইজ পুনরায় শুরু করুন",
                    color = CyberCyanNeon,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
