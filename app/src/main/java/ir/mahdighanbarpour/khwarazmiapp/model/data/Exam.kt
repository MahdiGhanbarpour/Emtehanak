package ir.mahdighanbarpour.khwarazmiapp.model.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exam(
    @SerializedName("authorName") val authorName: String,
    @SerializedName("authorPhoneNum") val authorPhoneNum: String?,
    @SerializedName("description") val description: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("grade") val grade: String,
    @SerializedName("id") val id: Int,
    @SerializedName("image") val image: String?,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("lesson") val lesson: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("rating") val rating: Int,
    @SerializedName("showQuestionAnswer") val showQuestionAnswer: Boolean,
    @SerializedName("showTotalPercent") val showTotalPercent: Boolean,
    @SerializedName("teacherMessage") val teacherMessage: String
) : Parcelable