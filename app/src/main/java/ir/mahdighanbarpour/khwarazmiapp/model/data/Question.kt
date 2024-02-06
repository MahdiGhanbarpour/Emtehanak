package ir.mahdighanbarpour.khwarazmiapp.model.data

data class Question(
    val questionId: Int,
    val question: String,
    val options: MutableList<QuestionOptions>
)

data class QuestionOptions(
    val option: String,
    val isCorrect: Boolean
)