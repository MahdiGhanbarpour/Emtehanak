package ir.mahdighanbarpour.khwarazmiapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_frequently_questions")
data class FrequentlyQuestions(

    @PrimaryKey
    val frequentlyQuestionsPage: String,
    val frequentlyQuestionsArray: List<FrequentlyQuestionsArray>
)

data class FrequentlyQuestionsArray(
    val question: String, val answer: String
)
