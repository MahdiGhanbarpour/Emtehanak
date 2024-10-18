package ir.mahdighanbarpour.emtehanak.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class AddQuestionOption(
    val isCorrect: Boolean,
    val option: String,
    val image: File?,
) : Parcelable
