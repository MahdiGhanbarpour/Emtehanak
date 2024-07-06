package ir.mahdighanbarpour.khwarazmiapp.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class AddQuestionAttachment(
    var image: File, var description: String
) : Parcelable