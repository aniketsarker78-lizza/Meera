package com.example.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class MeeraTTSService(private val context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = null

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking = _isSpeaking.asStateFlow()

  private val _isInitialized = MutableStateFlow(false)
  val isInitialized = _isInitialized.asStateFlow()

  init {
    tts = TextToSpeech(context.applicationContext, this)
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val banglaLocale = Locale("bn", "BD")
      val result = tts?.setLanguage(banglaLocale)

      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        // Fallback to generic Bangla or default
        val fallback = Locale("bn")
        tts?.setLanguage(fallback)
      }

      // Configure cute, sweet 18-yo voice tone (higher pitch, gentle rate)
      tts?.setPitch(1.25f)
      tts?.setSpeechRate(0.95f)

      tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
          _isSpeaking.value = true
        }

        override fun onDone(utteranceId: String?) {
          _isSpeaking.value = false
        }

        @Deprecated("Deprecated in Java")
        override fun onError(utteranceId: String?) {
          _isSpeaking.value = false
        }
      })

      _isInitialized.value = true
      Log.d("MeeraTTS", "Meera Bengali TTS initialized successfully")
    } else {
      Log.e("MeeraTTS", "Failed to initialize TTS engine")
    }
  }

  fun speak(text: String) {
    if (_isInitialized.value && tts != null) {
      val utteranceId = "meera_${System.currentTimeMillis()}"
      tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }
  }

  fun stop() {
    tts?.stop()
    _isSpeaking.value = false
  }

  fun shutdown() {
    tts?.stop()
    tts?.shutdown()
    tts = null
  }
}
