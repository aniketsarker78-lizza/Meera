package com.example.data

import com.example.data.model.JapaneseCharacter
import com.example.data.model.JapaneseQuizQuestion

object JapaneseTeacherModule {

  val hiraganaList = listOf(
    JapaneseCharacter("あ", "a", "আ", "あさ (asa)", "সকাল"),
    JapaneseCharacter("い", "i", "ই", "いぬ (inu)", "কুকুর"),
    JapaneseCharacter("う", "u", "উ", "うみ (umi)", "সমুদ্র"),
    JapaneseCharacter("え", "e", "এ", "えき (eki)", "স্টেশন"),
    JapaneseCharacter("お", "o", "ও", "おちゃ (ocha)", "সবুজ চা"),
    JapaneseCharacter("か", "ka", "কা", "かわ (kawa)", "নদী"),
    JapaneseCharacter("き", "ki", "কি", "き (ki)", "গাছ"),
    JapaneseCharacter("く", "ku", "কু", "くるま (kuruma)", "গাড়ি"),
    JapaneseCharacter("け", "ke", "কে", "けさ (kesa)", "আজ সকাল"),
    JapaneseCharacter("こ", "ko", "কো", "こども (kodomo)", "বাচ্চা / শিশু"),
    JapaneseCharacter("さ", "sa", "সা", "さくら (sakura)", "চেরি ফুল"),
    JapaneseCharacter("し", "shi", "শি", "しろ (shiro)", "সাদা"),
    JapaneseCharacter("す", "su", "সু", "すし (sushi)", "সুশি"),
    JapaneseCharacter("せ", "se", "সে", "せんせい (sensei)", "শিক্ষক / ওস্তাদ"),
    JapaneseCharacter("そ", "so", "সো", "そら (sora)", "আকাশ")
  )

  val katakanaList = listOf(
    JapaneseCharacter("ア", "a", "আ", "アイス (aisu)", "আইসক্রিম"),
    JapaneseCharacter("イ", "i", "ই", "インド (indo)", "ভারত"),
    JapaneseCharacter("ウ", "u", "উ", "ウサギ (usagi)", "খরগোশ"),
    JapaneseCharacter("エ", "e", "এ", "エアコン (eakon)", "এয়ার কন্ডিশনার"),
    JapaneseCharacter("オ", "o", "ও", "オレンジ (orenji)", "কমলালেবু"),
    JapaneseCharacter("カ", "ka", "কা", "カメラ (kamera)", "ক্যামেরা"),
    JapaneseCharacter("キ", "ki", "কি", "キー (kii)", "চাবি"),
    JapaneseCharacter("ク", "ku", "কু", "クラス (kurasu)", "ক্লাস"),
    JapaneseCharacter("ケ", "ke", "কে", "ケーキ (keeki)", "কেক"),
    JapaneseCharacter("コ", "ko", "কো", "コーヒー (koohii)", "কফি")
  )

  val jlptLevels = listOf(
    "N5 (প্রাথমিক বেসিক)",
    "N4 (দৈনন্দিন বাক্য গঠন)",
    "N3 (মধ্যবর্তী কথোপকথন)",
    "N2 (উচ্চতর জাপানি)",
    "N1 (প্রফেশনাল ও জটিল সাহিত্য)"
  )

  val n5Vocabulary = listOf(
    Triple("こんにちは", "Konnichiwa", "হ্যালো / শুভ অপরাহ্ন"),
    Triple("ありがとう ございます", "Arigatou gozaimasu", "আপনাকে অনেক ধন্যবাদ"),
    Triple("おはよう ございます", "Ohayou gozaimasu", "শুভ সকাল"),
    Triple("さようなら", "Sayounara", "বিদায়"),
    Triple("おねがい します", "Onegai shimasu", "অনুগ্রহ করে / প্লিজ"),
    Triple("ごめんなさい", "Gomennasai", "আমি ক্ষমাপ্রার্থী"),
    Triple("はい / いいえ", "Hai / Iie", "হ্যাঁ / না"),
    Triple("ともだち", "Tomodachi", "বন্ধু")
  )

  val sampleQuizQuestions = listOf(
    JapaneseQuizQuestion(
      prompt = "জাপানি শব্দ 'さくら' (Sakura)-এর বাংলা অর্থ কী?",
      options = listOf("গোলাপ", "চেরি ফুল", "পদ্ম ফুল", "সূর্যমুখী"),
      correctIndex = 1,
      explanationBengali = "সঠিক! 'さくら' (Sakura) মানে জাপানের জাতীয় ঐতিহ্যবাহী চেরি ফুল।"
    ),
    JapaneseQuizQuestion(
      prompt = "'せんせい' (Sensei) শব্দের অর্থ কী?",
      options = listOf("ছাত্র", "ডাক্তার", "শিক্ষক / ওস্তাদ", "সহকর্মী"),
      correctIndex = 2,
      explanationBengali = "একদম ঠিক! 'Sensei' শব্দের অর্থ শিক্ষক বা সম্মানীয় গুরু।"
    ),
    JapaneseQuizQuestion(
      prompt = "সকালের অভিবাদন জানাতে জাপানিতে কী বলা হয়?",
      options = listOf("Konnichiwa", "Konbanwa", "Ohayou gozaimasu", "Oyasuminasai"),
      correctIndex = 2,
      explanationBengali = "বাহ দারুণ! 'Ohayou gozaimasu' দিয়ে সকালে শুভ সকাল জানানো হয়।"
    ),
    JapaneseQuizQuestion(
      prompt = "'ありがとう' (Arigatou) শব্দের অর্থ কী?",
      options = listOf("ধন্যবাদ", "স্বাগতম", "মাফ করবেন", "বিদায়"),
      correctIndex = 0,
      explanationBengali = "সঠিক উত্তর! 'Arigatou' মানে ধন্যবাদ।"
    )
  )
}
