package ir.mahdighanbarpour.emtehanak.model.data

import com.google.gson.annotations.SerializedName

data class ExamsMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: ExamResult,
    @SerializedName("status")
    val status: Int
)

data class ExamResult(
    @SerializedName("exams")
    val exams: List<Exam>
)