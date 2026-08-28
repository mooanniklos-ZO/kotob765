package com.example.data

data class BookInfo(
    val title: String = "أثر الفكر الغربي المعاصر على المجتمع المسلم في الوقت الراهن",
    val subtitle: String = "دراسة فكرية وتاريخية وتأصيلية لمواجهة التغريب وتحصين الأمة",
    val author: String = "الدكتور / مالك عبدالرحمن الرميمة",
    val authorPhone: String = "771134103",
    val description: String = "بحث منهجي يعرض ماضي الأمة الإسلامية العريق ومحطات التحدي والغزو الفكري عبر العصور، مع بيان أسباب التراجع وسبل التحصن الجذري ونهضة الأمة."
)

data class BookSection(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val pageNumber: Int,
    val paragraphs: List<String>,
    val verses: List<String> = emptyList(),
    val keyPoints: List<String> = emptyList()
)

data class BookChapter(
    val id: String,
    val title: String,
    val subtitle: String,
    val chapterNumber: Int,
    val iconName: String,
    val sections: List<BookSection>
)

data class RenewerScholar(
    val id: String,
    val name: String,
    val lifespan: String,
    val role: String,
    val nominatedBy: String,
    val century: String
)

data class InvasionMatrixEntry(
    val id: String,
    val invasionType: String,      // نوع الغزو ووسائله
    val rootCauses: String,        // أسباب ومسببات الغزو
    val radicalSolution: String    // الحل الجذري والتحصن من الغزو
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class BookReferenceItem(
    val title: String,
    val authorOrSource: String,
    val details: String,
    val link: String = ""
)
