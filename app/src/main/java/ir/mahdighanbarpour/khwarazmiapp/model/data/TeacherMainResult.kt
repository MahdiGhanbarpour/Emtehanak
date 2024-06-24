package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class TeacherMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: TeacherResult,
    @SerializedName("status")
    val status: Int
)

data class TeacherResult(
    @SerializedName("teacher")
    val teacher: Teacher
)