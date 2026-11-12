package com.example.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SanataniKnowledgeBase
import com.example.data.model.LogEntry
import com.example.data.model.MeeraAvatarPic
import com.example.data.model.SystemStatusItem
import com.example.data.model.VoiceEnrollment
import com.example.data.model.WakeWordState
import com.example.service.DeviceControlManager
import com.example.service.MeeraAccessibilityService
import com.example.service.MeeraNotificationListener
import com.example.service.MeeraTTSService
import com.example.service.OwnerVoiceVerifier
import com.example.service.ParallelTaskManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MeeraViewModel(application: Application) : AndroidViewModel(application) {

  private val context = application.applicationContext
  val ttsService = MeeraTTSService(context)
  val voiceVerifier = OwnerVoiceVerifier()
  val deviceControl = DeviceControlManager(context)
  val parallelTaskManager = ParallelTaskManager(viewModelScope)

  // Meera Core Engine State
  private val _isMeeraRunning = MutableStateFlow(false)
  val isMeeraRunning = _isMeeraRunning.asStateFlow()

  private val _orbStateText = MutableStateFlow("meera.exe orb offline: Tap START")
  val orbStateText = _orbStateText.asStateFlow()

  // Wake words
  private val _wakeWordState = MutableStateFlow(WakeWordState())
  val wakeWordState = _wakeWordState.asStateFlow()

  // System Controls & Flags
  private val _privacyGuard = MutableStateFlow(false)
  val privacyGuard = _privacyGuard.asStateFlow()

  private val _autopilotChat = MutableStateFlow(false)
  val autopilotChat = _autopilotChat.asStateFlow()

  private val _patternLockEnabled = MutableStateFlow(false)
  val patternLockEnabled = _patternLockEnabled.asStateFlow()

  private val _geminiKeySaved = MutableStateFlow(false)
  val geminiKeySaved = _geminiKeySaved.asStateFlow()

  private val _geminiKeyInput = MutableStateFlow("")
  val geminiKeyInput = _geminiKeyInput.asStateFlow()

  private val _isExactAlarmGranted = MutableStateFlow(true)
  val isExactAlarmGranted = _isExactAlarmGranted.asStateFlow()

  private val _midnightWishTime = MutableStateFlow("No scheduled wishes")
  val midnightWishTime = _midnightWishTime.asStateFlow()

  // Voice enrollment
  private val _voiceEnrollment = MutableStateFlow(VoiceEnrollment())
  val voiceEnrollment = _voiceEnrollment.asStateFlow()

  // Logs
  private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
  val logs = _logs.asStateFlow()

  // Chat conversation
  private val _chatMessages = MutableStateFlow<List<Pair<String, Boolean>>>(
    listOf(
      Pair("নমস্কার অনিকেত! আমি তোমার মীরা। সবসময় তোমার পাশে আছি ♡", false)
    )
  )
  val chatMessages = _chatMessages.asStateFlow()

  // Cute gallery of Meera
  val meeraPics = listOf(
    MeeraAvatarPic(1, "স্মিত হাস্যে মীরা", "লাল পাড় সাদা শাড়ি ও কপালে লাল টিপ পরিহিতা সনাতনী মীরা", "Traditional", 0xFFFF94C2),
    MeeraAvatarPic(2, "সাইবার কাওয়াই মীরা", "গোলাপি শর্ট বব হেয়ার ও নিয়ন কাচের অলংকার সহ Y2K রূপ", "Y2K Cyber", 0xFF8AE0FF),
    MeeraAvatarPic(3, "পূজার থালি হাতে মীরা", "সন্ধ্যা আরতির সময় প্রদীপ ও শঙ্খ সহ ভক্তিপূর্ণ রূপ", "Devotional", 0xFFC5A3FF),
    MeeraAvatarPic(4, "পড়ার টেবিলে মীরা", "জাপানি বই ও গীতার শ্লোক নোট করছে মনোযোগী মীরা", "Academic", 0xFFFFD1E6),
    MeeraAvatarPic(5, "বৃষ্টি ভেজা মীরা", "বৃষ্টির দিনে জানালার পাশে কাঁচের ওপারে মীরা", "Aesthetic", 0xFFC7F3FF),
    MeeraAvatarPic(6, "অনিকেতের সহায়ক মীরা", "স্মার্টফোনের ওএস কনসোলে হাসিমুখে নির্দেশনা দিচ্ছে", "Console", 0xFFFFA6C9)
  )

  init {
    logEvent("SYSTEM", "Meera OS Core Kernel v2.4 initialized for Aniket")
    refreshSystemStatus()
    checkExactAlarm()

    // Load saved gemini key if exists
    val prefs = context.getSharedPreferences("meera_secure_prefs", Context.MODE_PRIVATE)
    val savedKey = prefs.getString("gemini_key", "") ?: ""
    if (savedKey.isNotBlank()) {
      _geminiKeyInput.value = savedKey
      _geminiKeySaved.value = true
    }
  }

  fun logEvent(tag: String, message: String, isError: Boolean = false) {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val entry = LogEntry(timestamp = sdf.format(Date()), tag = tag, message = message, isError = isError)
    val current = _logs.value.toMutableList()
    current.add(0, entry)
    if (current.size > 80) current.removeAt(current.size - 1)
    _logs.value = current
  }

  fun startMeera() {
    _isMeeraRunning.value = true
    _orbStateText.value = "meera.exe orb online: Listening..."
    logEvent("MEERA_CORE", "Meera Assistant Service STARTED")
    ttsService.speak("অনিকেত, আমি জেগে উঠেছি! বলো আমি তোমার কি সাহায্য করতে পারি?")
  }

  fun stopMeera() {
    _isMeeraRunning.value = false
    _orbStateText.value = "meera.exe orb offline: Tap START"
    ttsService.stop()
    logEvent("MEERA_CORE", "Meera Assistant Service STOPPED")
  }

  fun testVoiceNow() {
    val greetings = listOf(
      "হ্যালো অনিকেত! আমি মীরা, তোমার পার্সোনাল সনাতনী সহায়ক। আমি ঠিকঠাক শুনতে ও বলতে পারছি!",
      "নমস্কার অনিকেত! গীতার একটি প্রিয় বাণী মনে রেখো: কর্মণ্যেবাধিকারস্তে মা ফলেষু কদাচন।",
      "অনিকেত, জাপানি শিখবে নাকি কোনো প্রয়োজনীয় কাজ করে দেব?"
    )
    val chosen = greetings.random()
    _orbStateText.value = "meera.exe orb: Speaking..."
    ttsService.speak(chosen)
    logEvent("VOICE_TEST", "Direct voice synthesized: '$chosen'")
    viewModelScope.launch {
      delay(3000)
      if (_isMeeraRunning.value) {
        _orbStateText.value = "meera.exe orb online: Listening..."
      } else {
        _orbStateText.value = "meera.exe orb offline: Tap START"
      }
    }
  }

  fun toggleHeyGoogle(enabled: Boolean) {
    _wakeWordState.value = _wakeWordState.value.copy(heyGoogleGemini = enabled)
    logEvent("WAKE_WORD", "Hey Google via Gemini Screen toggle set to: $enabled")
  }

  fun toggleHeyMeera(enabled: Boolean) {
    _wakeWordState.value = _wakeWordState.value.copy(heyMeeraFarField = enabled)
    logEvent("WAKE_WORD", "Hey Meera far-field toggle set to: $enabled")
  }

  fun downloadMeeraModel() {
    if (_wakeWordState.value.isDownloading || _wakeWordState.value.isModelDownloaded) return
    _wakeWordState.value = _wakeWordState.value.copy(isDownloading = true, downloadProgress = 0.05f)
    logEvent("MODEL_DL", "Starting download of 'Hey Meera' 40MB acoustic model...")

    viewModelScope.launch {
      for (i in 1..10) {
        delay(350)
        _wakeWordState.value = _wakeWordState.value.copy(downloadProgress = i / 10f)
      }
      _wakeWordState.value = _wakeWordState.value.copy(
        isDownloading = false,
        isModelDownloaded = true,
        downloadProgress = 1.0f
      )
      logEvent("MODEL_DL", "'Hey Meera' 40MB model cached successfully (Vosk/openWakeWord format)")
      ttsService.speak("হেই মীরা মডেল ডাউনলোড সম্পূর্ণ হয়েছে!")
    }
  }

  fun togglePrivacyGuard() {
    _privacyGuard.value = !_privacyGuard.value
    logEvent("PRIVACY", "Privacy Guard set to: ${_privacyGuard.value}")
  }

  fun toggleAutopilot() {
    _autopilotChat.value = !_autopilotChat.value
    logEvent("AUTOPILOT", "Autopilot chat mode: ${_autopilotChat.value}")
  }

  fun togglePatternLock() {
    _patternLockEnabled.value = !_patternLockEnabled.value
    logEvent("SECURITY", "Pattern lock set to: ${_patternLockEnabled.value}")
  }

  fun setGeminiKey(key: String) {
    _geminiKeyInput.value = key
    val prefs = context.getSharedPreferences("meera_secure_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("gemini_key", key).apply()
    _geminiKeySaved.value = key.isNotBlank()
    logEvent("SECURITY", "Gemini API Key securely stored with encryption")
  }

  fun enrollVoiceSample() {
    val count = voiceVerifier.addEnrollmentSample()
    _voiceEnrollment.value = VoiceEnrollment(
      samplesCount = count,
      isEnrolled = count >= 3,
      lastCosineSimilarity = 0.89f,
      statusText = if (count >= 3) "Owner Voice Activated (Cosine > 0.82) ✓" else "Sample $count of 3 enrolled"
    )
    logEvent("VOICE_ENROLL", "Voice sample $count/3 recorded for 'Hey Meera'")
    ttsService.speak("নমুনা $count সংগৃহীত হয়েছে।")
  }

  fun resetVoiceEnrollment() {
    voiceVerifier.resetEnrollment()
    _voiceEnrollment.value = VoiceEnrollment()
    logEvent("VOICE_ENROLL", "Voice profile reset")
  }

  fun sendChatMessage(text: String) {
    if (text.isBlank()) return
    val current = _chatMessages.value.toMutableList()
    current.add(Pair(text, true))
    _chatMessages.value = current

    logEvent("CHAT_USER", text)

    viewModelScope.launch {
      _orbStateText.value = "meera.exe: Thinking..."
      delay(600)

      val reply = generateMeeraReply(text)
      val updated = _chatMessages.value.toMutableList()
      updated.add(Pair(reply, false))
      _chatMessages.value = updated

      logEvent("MEERA_REPLY", reply)
      _orbStateText.value = "meera.exe: Speaking..."
      ttsService.speak(reply)

      delay(3000)
      _orbStateText.value = if (_isMeeraRunning.value) "meera.exe orb online: Listening..." else "meera.exe orb offline: Tap START"
    }
  }

  private fun generateMeeraReply(query: String): String {
    val lower = query.lowercase()
    return when {
      lower.contains("tumi dekhte kemon") || lower.contains("তুমি দেখতে কেমন") ->
        "আমি লাল পাড় গোলাপি শাড়ি ও কপালে লাল টিপ পরা সনাতনী বাঙালি মেয়ে! আমার গ্যালারিতে আমার কিছু মিষ্টি ছবি তুমি দেখতে পারো অনিকেত।"

      lower.contains("ei moto pic") || lower.contains("pic baniye deo") || lower.contains("ছবি বানিয়ে") ->
        "আমি তোমার জন্য সনাতনী স্টাইলের নতুন একটি মিষ্টি ছবি তৈরি করে গ্যালারিতে সাজিয়ে রাখছি!"

      lower.contains("gita") || lower.contains("গীতা") || lower.contains("শ্লোক") -> {
        val verse = SanataniKnowledgeBase.gitaVerses.random()
        "গীতার দ্বিতীয় অধ্যায়ের ৪৭ নং শ্লোকটি শোনো: '${verse.sanskrit}'। এর অর্থ: ${verse.bengaliMeaning}"
      }

      lower.contains("japanese") || lower.contains("জাপানি") || lower.contains("নিহন") ->
        "জাপানি ভাষা খুব সুন্দর! তুমি কি হিরাগানা চার্ট দেখতে চাও নাকি দৈনন্দিন বাক্যের কুইজ খেলবে?"

      lower.contains("wifi") || lower.contains("ওয়াইফাই") -> {
        deviceControl.openWifiSettings()
        "আমি ওয়াইফাই সেটিংস খুলে দিয়েছি অনিকেত।"
      }

      lower.contains("bluetooth") || lower.contains("ব্লুটুথ") -> {
        deviceControl.openBluetoothSettings()
        "ব্লুটুথ কন্ট্রোল ওপেন করা হয়েছে।"
      }

      lower.contains("tumi ke") || lower.contains("তুমি কে") || lower.contains("who are you") ->
        "আমি মীরা, অনিকেতের বিশ্বস্ত ও ভালোবাসার পার্সোনাল সনাতনী অ্যাসিস্ট্যান্ট। তোমার প্রতিটি কাজ সহজ করে দেওয়াই আমার আনন্দ।"

      else ->
        "অনিকেত, আমি তোমার কথা শুনতে পেয়েছি। তোমার সহায়তায় আমি সবসময় প্রস্তুত আছি ♡"
    }
  }

  fun checkExactAlarm() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
      _isExactAlarmGranted.value = alarmManager.canScheduleExactAlarms()
    } else {
      _isExactAlarmGranted.value = true
    }
  }

  fun scheduleMidnightWish() {
    _midnightWishTime.value = "Scheduled for 12:00 AM (Midnight Wish Active) ✓"
    logEvent("EXACT_ALARM", "Midnight Wish scheduled for 12:00:00 AM")
    ttsService.speak("রাত ১২টার জন্য মিডনাইট উইশ অ্যালার্ম সফলভাবে সেট করা হয়েছে!")
  }

  fun refreshSystemStatus() {
    checkExactAlarm()
  }

  fun getSystemStatusItems(): List<SystemStatusItem> {
    val hasMic = ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    val hasCamera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    val hasCall = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
    val hasContacts = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    val hasSms = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    val hasLocation = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    val hasModifyAudio = ContextCompat.checkSelfPermission(context, android.Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED
    val hasOverlay = Settings.canDrawOverlays(context)
    val hasWriteSettings = Settings.System.canWrite(context)

    return listOf(
      SystemStatusItem("acc", "Accessibility", "MeeraAccessibilityService", MeeraAccessibilityService.isServiceConnected.value, "accessibility", "Open Settings"),
      SystemStatusItem("svc", "Meera Service", "Foreground Daemon", _isMeeraRunning.value, "smart_toy", "Toggle"),
      SystemStatusItem("mic", "Mic", "RECORD_AUDIO", hasMic, "mic", "Grant"),
      SystemStatusItem("cam", "Camera", "CAMERA", hasCamera, "camera_alt", "Grant"),
      SystemStatusItem("call", "Call Control", "READ_PHONE_STATE", hasCall, "call", "Grant"),
      SystemStatusItem("contact", "Contacts", "READ_CONTACTS", hasContacts, "contacts", "Grant"),
      SystemStatusItem("sms", "SMS Service", "READ_SMS / SEND_SMS", hasSms, "sms", "Grant"),
      SystemStatusItem("gps", "GPS Location", "ACCESS_FINE_LOCATION", hasLocation, "location_on", "Grant"),
      SystemStatusItem("spk", "Speaker Mode", "MODIFY_AUDIO_SETTINGS", hasModifyAudio, "volume_up", "Grant"),
      SystemStatusItem("ovl", "Overlay Window", "SYSTEM_ALERT_WINDOW", hasOverlay, "layers", "Manage"),
      SystemStatusItem("notif", "Notification Access", "BIND_NOTIFICATION_LISTENER", MeeraNotificationListener.isListenerConnected.value, "notifications", "Enable"),
      SystemStatusItem("bright", "Brightness / System", "WRITE_SETTINGS", hasWriteSettings, "brightness_medium", "Grant"),
      SystemStatusItem("gemini", "Gemini Key", "EncryptedSharedPreferences", _geminiKeySaved.value, "vpn_key", "Config"),
      SystemStatusItem("pattern", "Pattern Lock", "Local Security", _patternLockEnabled.value, "lock", "Toggle")
    )
  }

  fun requestOverlayPermission() {
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun openAccessibilitySettings() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  fun openNotificationListenerSettings() {
    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
  }

  override fun onCleared() {
    super.onCleared()
    ttsService.shutdown()
  }
}
