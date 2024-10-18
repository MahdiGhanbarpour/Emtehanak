package ir.mahdighanbarpour.emtehanak.model.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    @SerializedName("grade")
    val grade: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pdf")
    val pdf: String
) : Parcelable