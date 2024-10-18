package ir.mahdighanbarpour.emtehanak.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddQuestion(
    val attachment: List<AddQuestionAttachment>,
    val options: List<AddQuestionOption>,
    val question: String
) : Parcelable
