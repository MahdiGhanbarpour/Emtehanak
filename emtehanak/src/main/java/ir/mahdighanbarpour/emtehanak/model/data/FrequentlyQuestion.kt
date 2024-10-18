package ir.mahdighanbarpour.emtehanak.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_frequently_questions")
data class FrequentlyQuestion(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("page")
    val page: String,

    @PrimaryKey
    @SerializedName("question")
    val question: String
)