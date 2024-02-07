package ir.mahdighanbarpour.khwarazmiapp.model.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exam(
    @SerializedName("authorName") val authorName: String,
    @SerializedName("authorPhoneNum") val authorPhoneNum: String,
    @SerializedName("description") val description: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("grade") val grade: String,
    @SerializedName("image") val image: String,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("rating") val rating: Double
) : Parcelable