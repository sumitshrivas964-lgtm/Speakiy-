package com.example.data.model

import java.util.Locale

data class Language(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val localeTag: String,
    val greeting: String,
    val starterTopics: List<String>
) {
    fun toLocale(): Locale {
        return try {
            Locale.forLanguageTag(localeTag)
        } catch (e: Exception) {
            Locale.ENGLISH
        }
    }

    companion object {
        val ALL_LANGUAGES: List<Language> = listOf(
            Language("en", "English", "English", "🇺🇸", "en-US", "Hello! What would you like to talk about today?", listOf("Daily Routine", "Travel Experiences", "Tech & Future", "Favorite Books")),
            Language("es", "Spanish", "Español", "🇪🇸", "es-ES", "¡Hola! ¿De qué te gustaría hablar hoy?", listOf("Comida típica", "Planes de fin de semana", "Cultura y música", "El clima")),
            Language("fr", "French", "Français", "🇫🇷", "fr-FR", "Bonjour ! De quoi aimeriez-vous parler aujourd'hui ?", listOf("La gastronomie", "Voyages en Europe", "Cinéma français", "Vie quotidienne")),
            Language("de", "German", "Deutsch", "🇩🇪", "de-DE", "Hallo! Worüber möchtest du heute sprechen?", listOf("Hobbys & Freizeit", "Deutsche Städte", "Arbeitsalltag", "Umweltschutz")),
            Language("it", "Italian", "Italiano", "🇮🇹", "it-IT", "Ciao! Di cosa ti piacerebbe parlare oggi?", listOf("La cucina italiana", "Vacanze al mare", "Arte e storia", "La mia famiglia")),
            Language("pt", "Portuguese", "Português", "🇧🇷", "pt-BR", "Olá! Sobre o que você gostaria de conversar hoje?", listOf("Música brasileira", "Culinária local", "Viagens e praias", "Futebol e esportes")),
            Language("ja", "Japanese", "日本語", "🇯🇵", "ja-JP", "こんにちは！今日は何について話しましょうか？", listOf("アニメとマンガ", "日本の四季", "おすすめの旅行先", "日常会話")),
            Language("ko", "Korean", "한국어", "🇰🇷", "ko-KR", "안녕하세요! 오늘 어떤 이야기를 나누고 싶으신가요?", listOf("K-드라마와 음악", "한국 음식", "주말 일상", "취미 생활")),
            Language("zh", "Mandarin Chinese", "中文", "🇨🇳", "zh-CN", "你好！今天你想聊些什么话题呢？", listOf("中国传统节日", "特色美食", "工作与学习", "旅行见闻")),
            Language("ar", "Arabic", "العربية", "🇸🇦", "ar-SA", "مرحباً! ما الذي ترغب في التحدث عنه اليوم؟", listOf("الثقافة والتقاليد", "الأطباق الشعبية", "الحياة اليومية", "السياحة")),
            Language("hi", "Hindi", "हिन्दी", "🇮🇳", "hi-IN", "नमस्ते! आज आप किस विषय पर बात करना चाहेंगे?", listOf("भारतीय त्यौहार", "पसंदीदा व्यंजन", "यात्रा और संस्कृति", "दैनिक जीवन")),
            Language("ru", "Russian", "Русский", "🇷🇺", "ru-RU", "Привет! О чём ты хочешь поговорить сегодня?", listOf("Путешествия", "Русская литература", "Праздники", "Погода и природа")),
            Language("nl", "Dutch", "Nederlands", "🇳🇱", "nl-NL", "Hallo! Waar wil je het vandaag over hebben?", listOf("Steden in Nederland", "Fietsen en cultuur", "Vrijetijdsbesteding", "Het weer")),
            Language("tr", "Turkish", "Türkçe", "🇹🇷", "tr-TR", "Merhaba! Bugün ne hakkında konuşmak istersin?", listOf("Türk kahvesi ve yemekler", "Gezilecek yerler", "Gelenekler", "Hafta sonu planları")),
            Language("sv", "Swedish", "Svenska", "🇸🇪", "sv-SE", "Hej! Vad vill du prata om idag?", listOf("Fika och traditioner", "Svensk natur", "Musik och film", "Årstiderna")),
            Language("pl", "Polish", "Polski", "🇵🇱", "pl-PL", "Cześć! O czym chciałbyś dzisiaj porozmawiać?", listOf("Polska kuchnia", "Podróże i góry", "Kultura i historia", "Plany na weekend")),
            Language("vi", "Vietnamese", "Tiếng Việt", "🇻🇳", "vi-VN", "Xin chào! Hôm nay bạn muốn trò chuyện về chủ đề gì?", listOf("Ẩm thực đường phố", "Du lịch Việt Nam", "Công việc và cuộc sống", "Sở thích cá nhân")),
            Language("th", "Thai", "ภาษาไทย", "🇹🇭", "th-TH", "สวัสดีครับ/ค่ะ วันนี้อยากคุยเรื่องอะไรดีครับ/ค่ะ?", listOf("อาหารไทยรสเด็ด", "สถานที่ท่องเที่ยว", "เทศกาลสงกรานต์", "ชีวิตประจำวัน")),
            Language("el", "Greek", "Ελληνικά", "🇬🇷", "el-GR", "Γεια σας! Για τι θα θέλατε να μιλήσουμε σήμερα;", listOf("Ελληνικά νησιά", "Παραδοσιακή κουζίνα", "Μυθολογία και ιστορία", "Καθημερινότητα")),
            Language("id", "Indonesian", "Bahasa Indonesia", "🇮🇩", "id-ID", "Halo! Mau ngobrol tentang apa hari ini?", listOf("Kuliner nusantara", "Wisata pantai", "Kegiatan santai", "Musik favorit")),
            Language("bn", "Bengali", "বাংলা", "🇧🇩", "bn-BD", "হ্যালো! আজ আপনি কি বিষয়ে কথা বলতে চান?", listOf("বাঙালি সংস্কৃতি", "উৎসব ও আনন্দ", "প্রিয় বই ও গান", "ভ্রমণ অভিজ্ঞতা")),
            Language("tl", "Tagalog (Filipino)", "Tagalog", "🇵🇭", "fil-PH", "Kumusta! Anong gusto mong pag-usapan ngayon?", listOf("Mga pagkaing Pinoy", "Kultura at pistahan", "Paboritong pasyalan", "Kuwentong araw-araw")),
            Language("uk", "Ukrainian", "Українська", "🇺🇦", "uk-UA", "Привіт! Про що ти хочеш поговорити сьогодні?", listOf("Традиції та свята", "Українська кухня", "Улюблені міста", "Повсякденне життя")),
            Language("cs", "Czech", "Čeština", "🇨🇿", "cs-CZ", "Ahoj! O čem bys dnes rád mluvil?", listOf("Památky Prahy", "Česká kuchyně", "Plány a koníčky", "Cestování")),
            Language("da", "Danish", "Dansk", "🇩🇰", "da-DK", "Hej! Hvad vil du gerne tale om i dag?", listOf("Hygge og livsstil", "Dansk design", "Weekendplaner", "Rejser")),
            Language("fi", "Finnish", "Suomi", "🇫🇮", "fi-FI", "Hei! Mistä haluaisit puhua tänään?", listOf("Saunakulttuuri", "Suomen luonto", "Harrastukset", "Arkielämä")),
            Language("no", "Norwegian", "Norsk", "🇳🇴", "nb-NO", "Hei! Hva vil du snakke om i dag?", listOf("Friluftsliv og fjell", "Norske tradisjoner", "Musikk", "Reiser i Norden")),
            Language("he", "Hebrew", "עברית", "🇮🇱", "he-IL", "שלום! על מה תרצה לדבר היום?", listOf("טיולים בארץ", "אוכל ותרבות", "שגרת יום-יום", "מוזיקה ישראלית")),
            Language("ro", "Romanian", "Română", "🇷🇴", "ro-RO", "Bună! Despre ce ai vrea să vorbim astăzi?", listOf("Tradiții românești", "Locuri de vizitat", "Bucătăria tradițională", "Planuri de viitor")),
            Language("hu", "Hungarian", "Magyar", "🇭🇺", "hu-HU", "Szia! Miről szeretnél ma beszélgetni?", listOf("Budapesti látványosságok", "Magyar ételek", "Szabadidő és sport", "Hétköznapok")),
            Language("sw", "Swahili", "Kiswahili", "🇰🇪", "sw-KE", "Jambo! Ungependa kuzungumzia nini leo?", listOf("Utamaduni wa Kiafrika", "Wanyamapori na hifadhi", "Muziki na ngoma", "Maisha ya kila siku")),
            Language("fa", "Persian (Farsi)", "فارسی", "🇮🇷", "fa-IR", "سلام! امروز مایلید درباره چه چیزی صحبت کنیم؟", listOf("شعر و ادبیات فارسی", "غذاهای سنتی", "سفرهای خاطره‌انگیز", "زندگی روزمره")),
            Language("ms", "Malay", "Bahasa Melayu", "🇲🇾", "ms-MY", "Hai! Apakah yang anda ingin bincangkan hari ini?", listOf("Makanan kegemaran", "Tempat percutian menarik", "Budaya dan adat", "Rancangan hujung minggu"))
        )

        fun findByCode(code: String): Language {
            return ALL_LANGUAGES.find { it.code.equals(code, ignoreCase = true) } ?: ALL_LANGUAGES.first()
        }
    }
}
