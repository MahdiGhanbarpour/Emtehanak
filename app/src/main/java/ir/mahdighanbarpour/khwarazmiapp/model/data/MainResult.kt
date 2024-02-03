package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class MainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: Result,
    @SerializedName("status")
    val status: Int
)

data class Result(
    @SerializedName("student")
    val student: Student
)