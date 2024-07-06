package ir.mahdighanbarpour.khwarazmiapp.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddQuestion(
    val attachment: List<AddQuestionAttachment>, val options: List<Option>, val question: String
) : Parcelable
