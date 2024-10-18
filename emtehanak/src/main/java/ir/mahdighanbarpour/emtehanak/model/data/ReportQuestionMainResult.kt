package ir.mahdighanbarpour.emtehanak.model.data

import com.google.gson.annotations.SerializedName

data class ReportQuestionMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("status")
    val status: Int
)