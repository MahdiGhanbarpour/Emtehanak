package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("grade")
    val grade: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)