package ir.mahdighanbarpour.emtehanak.model.data

import com.google.gson.annotations.SerializedName

data class AddExamMainResult(
    @SerializedName("massage") val massage: String,
    @SerializedName("result") val result: AddExamResult,
    @SerializedName("status") val status: Int
)

data class AddExamResult(
    @SerializedName("id") val id: Int
)