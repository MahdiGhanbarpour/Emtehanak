package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class LessonsMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val lessonsResult: LessonsResult,
    @SerializedName("status")
    val status: Int
)

data class LessonsResult(
    @SerializedName("lessons")
    val lessons: List<Lesson>
)