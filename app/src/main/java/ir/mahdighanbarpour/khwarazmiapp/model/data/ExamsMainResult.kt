package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class ExamsMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: ExamsResult,
    @SerializedName("status")
    val status: Int
)

data class ExamsResult(
    @SerializedName("exams")
    val exams: ArrayList<Exam>
)