package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.SystemStatusItem
import com.example.ui.components.ControlPanelSection
import com.example.ui.components.FilesSection
import com.example.ui.components.FooterSection
import com.example.ui.components.ShizukuPrivacyMidnightSection
import com.example.ui.components.SystemStatusGrid
import com.example.ui.components.WakeWordsSection
import com.example.ui.components.Y2kOrbVisualizer
import com.example.viewmodel.MeeraViewModel

@Composable
fun ConsoleScreen(
  viewModel: MeeraViewModel,
  onRequestPermission: (String) -> Unit,
  onNavigateToVoiceChat: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isRunning by viewModel.isMeeraRunning.collectAsState()
  val orbStatus by viewModel.orbStateText.collectAsState()
  val wakeState by viewModel.wakeWordState.collectAsState()
  val privacyGuard by viewModel.privacyGuard.collectAsState()
  val autopilot by viewModel.autopilotChat.collectAsState()
  val isExactAlarmGranted by viewModel.isExactAlarmGranted.collectAsState()
  val midnightWishStatus by viewModel.midnightWishTime.collectAsState()
  val patternLock by viewModel.patternLockEnabled.collectAsState()
  val geminiKeySaved by viewModel.geminiKeySaved.collectAsState()
  val geminiKeyInput by viewModel.geminiKeyInput.collectAsState()
  val logs by viewModel.logs.collectAsState()

  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. meera.exe with pulsing animated Y2K cyber orb
    Y2kOrbVisualizer(
      isRunning = isRunning,
      statusText = orbStatus,
      onOrbClick = {
        if (!isRunning) viewModel.startMeera() else viewModel.testVoiceNow()
      }
    )

    // 2. control_panel (START Meera, STOP Meera, Test now, Learn Gemini)
    ControlPanelSection(
      isRunning = isRunning,
      onStartClick = { viewModel.startMeera() },
      onStopClick = { viewModel.stopMeera() },
      onTestNowClick = { viewModel.testVoiceNow() },
      onLearnGeminiClick = { viewModel.sendChatMessage("মীরা, জেমিনি ও এআই দিয়ে তুমি কি কি করতে পারো?") },
      geminiScreenDefault = wakeState.heyGoogleGemini,
      onToggleGeminiScreenDefault = { viewModel.toggleHeyGoogle(it) }
    )

    // 3. wake_words (Hey Google via Gemini ON, Hey Meera far-field OFF + 40MB model download)
    WakeWordsSection(
      wakeState = wakeState,
      onToggleHeyGoogle = { viewModel.toggleHeyGoogle(it) },
      onToggleHeyMeera = { viewModel.toggleHeyMeera(it) },
      onDownloadModelClick = { viewModel.downloadMeeraModel() }
    )

    // 4. shizuku.sys + privacy.sys + midnight_wish + autopilot.chat
    ShizukuPrivacyMidnightSection(
      isShizukuRunning = true,
      onGrantAllPermissions = {
        onRequestPermission("android.permission.RECORD_AUDIO")
      },
      onShellTestClick = {
        viewModel.logEvent("SHELL", "uid=10243(u0_a243) gid=10243 groups=10243 context=u:r:untrusted_app:s0")
      },
      privacyGuard = privacyGuard,
      onTogglePrivacyGuard = { viewModel.togglePrivacyGuard() },
      isExactAlarmGranted = isExactAlarmGranted,
      midnightWishStatus = midnightWishStatus,
      onScheduleMidnightWish = { viewModel.scheduleMidnightWish() },
      onRequestExactAlarm = { viewModel.checkExactAlarm() },
      autopilotChat = autopilot,
      onToggleAutopilot = { viewModel.toggleAutopilot() }
    )

    // 5. system_status 2-column grid
    SystemStatusGrid(
      items = viewModel.getSystemStatusItems(),
      geminiKey = geminiKeyInput,
      isGeminiKeySaved = geminiKeySaved,
      onSaveGeminiKey = { viewModel.setGeminiKey(it) },
      isPatternEnabled = patternLock,
      onTogglePattern = { viewModel.togglePatternLock() },
      onItemActionClick = { item ->
        when (item.id) {
          "acc" -> viewModel.openAccessibilitySettings()
          "notif" -> viewModel.openNotificationListenerSettings()
          "ovl" -> viewModel.requestOverlayPermission()
          "bright" -> viewModel.deviceControl.requestWriteSettingsPermission()
          "svc" -> if (isRunning) viewModel.stopMeera() else viewModel.startMeera()
          "mic" -> onRequestPermission(android.Manifest.permission.RECORD_AUDIO)
          "cam" -> onRequestPermission(android.Manifest.permission.CAMERA)
          "call" -> onRequestPermission(android.Manifest.permission.READ_PHONE_STATE)
          "contact" -> onRequestPermission(android.Manifest.permission.READ_CONTACTS)
          "sms" -> onRequestPermission(android.Manifest.permission.READ_SMS)
          "gps" -> onRequestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
          "spk" -> onRequestPermission(android.Manifest.permission.MODIFY_AUDIO_SETTINGS)
        }
      }
    )

    // 6. files: setup.bat, pattern_lock, memory_backup, log.txt
    FilesSection(
      logs = logs,
      isPatternEnabled = patternLock,
      onRunSetupBat = {
        viewModel.logEvent("BATCH", "setup.bat successfully executed all initialization hooks")
        viewModel.ttsService.speak("সেটআপ ব্যাট সফলভাবে এক্সিকিউট করা হয়েছে।")
      }
    )

    // 7. footer made for aniket ♡
    FooterSection()

    Spacer(modifier = Modifier.height(30.dp))
  }
}
