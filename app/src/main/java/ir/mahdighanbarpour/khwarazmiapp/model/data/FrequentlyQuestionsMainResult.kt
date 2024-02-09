package ir.mahdighanbarpour.khwarazmiapp.model.data


import com.google.gson.annotations.SerializedName

data class FrequentlyQuestionsMainResult(
    @SerializedName("massage")
    val massage: String,
    @SerializedName("result")
    val result: FrequentlyQuestionsResult,
    @SerializedName("status")
    val status: Int
)

data class FrequentlyQuestionsResult(
    @SerializedName("frequentlyQuestions")
    val frequentlyQuestions: List<FrequentlyQuestion>
)