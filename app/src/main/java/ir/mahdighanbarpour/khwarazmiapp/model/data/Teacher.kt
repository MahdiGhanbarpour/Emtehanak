package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class Teacher(
    @SerializedName("activityYear")
    val activityYear: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("studyField")
    val studyField: String
)