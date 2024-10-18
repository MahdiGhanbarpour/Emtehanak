package ir.mahdighanbarpour.emtehanak.model.data

import com.google.gson.annotations.SerializedName

data class QuestionMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: QuestionResult,
    @SerializedName("status")
    val status: Int
)

data class QuestionResult(
    @SerializedName("questions")
    val questions: List<Question>
)