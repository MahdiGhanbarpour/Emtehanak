package ir.mahdighanbarpour.khwarazmiapp.model.data

data class Question(
    val questionId: Int,
    val question: String,
    val options: MutableList<QuestionOptions>,
    val attachments: ArrayList<Attachment>
)

data class QuestionOptions(
    val option: String, val isCorrect: Boolean
)

data class Attachment(
    val detail: String, val image: String
)