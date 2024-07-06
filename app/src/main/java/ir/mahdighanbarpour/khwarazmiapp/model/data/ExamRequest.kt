package ir.mahdighanbarpour.khwarazmiapp.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExamRequest(
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
    val showQuestionAnswer: Boolean,
    var questions: List<AddQuestion>
) : Parcelable
