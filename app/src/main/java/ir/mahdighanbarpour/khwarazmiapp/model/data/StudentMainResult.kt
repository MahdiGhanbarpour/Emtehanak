package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class StudentMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: StudentResult,
    @SerializedName("status")
    val status: Int
)

data class StudentResult(
    @SerializedName("student")
    val student: Student
)