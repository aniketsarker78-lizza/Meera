package com.example.data.model

data class SystemStatusItem(
  val id: String,
  val name: String,
  val permissionOrService: String,
  val isGranted: Boolean,
  val iconName: String,
  val actionHint: String
)

data class LogEntry(
  val id: String = java.util.UUID.randomUUID().toString(),
  val timestamp: String,
  val tag: String,
  val message: String,
  val isError: Boolean = false
)

data class WakeWordState(
  val heyGoogleGemini: Boolean = true,
  val heyMeeraFarField: Boolean = false,
  val isModelDownloaded: Boolean = false,
  val isDownloading: Boolean = false,
  val downloadProgress: Float = 0f
)

data class VoiceEnrollment(
  val samplesCount: Int = 0,
  val isEnrolled: Boolean = false,
  val lastCosineSimilarity: Float = 0f,
  val statusText: String = "Enroll 3x 'Hey Meera' to activate owner lock"
)

data class SanataniVerse(
  val chapter: Int,
  val verse: Int,
  val sanskrit: String,
  val bengaliMeaning: String,
  val theme: String
)

data class VedicMantra(
  val name: String,
  val sanskrit: String,
  val bengaliTransliteration: String,
  val bengaliMeaning: String,
  val timeOrOccasion: String
)

data class JapaneseCharacter(
  val kana: String,
  val romaji: String,
  val bengaliPronunciation: String,
  val sampleWord: String,
  val sampleMeaningBengali: String
)

data class JapaneseQuizQuestion(
  val prompt: String,
  val options: List<String>,
  val correctIndex: Int,
  val explanationBengali: String
)

enum class TaskStatus {
  IDLE, RUNNING, SUCCESS, FAILED
}

data class ParallelTaskItem(
  val id: String,
  val title: String,
  val icon: String,
  val status: TaskStatus = TaskStatus.IDLE,
  val progress: Float = 0f,
  val resultMessage: String = ""
)

data class MeeraAvatarPic(
  val id: Int,
  val titleBengali: String,
  val descriptionBengali: String,
  val tag: String,
  val primaryColorHex: Long
)

data class CallLogEntry(
  val id: String = java.util.UUID.randomUUID().toString(),
  val callerNumber: String,
  val timestamp: String,
  val isAutoAnswered: Boolean,
  val audioTranscript: String,
  val status: String
)
