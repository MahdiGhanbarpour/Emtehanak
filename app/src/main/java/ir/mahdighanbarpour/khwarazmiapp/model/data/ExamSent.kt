package ir.mahdighanbarpour.khwarazmiapp.model.data

data class ExamSent(
    val name: String,
    val description: String,
    val grade: String,
    val lesson: String,
    val authorName: String,
    val authorPhoneNumber: String,
    val price: String,
    val difficulty: String,
    val teacherMessage: String,
    val showTotalPercent: Boolean,
    val showQuestionAnswer: Boolean
)
