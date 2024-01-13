package ir.mahdighanbarpour.khwarazmiapp.model.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestions

@Dao
interface FrequentlyQuestionsDao {

    @Query("SELECT * FROM table_frequently_questions WHERE frequentlyQuestionsPage = :page")
    fun getFrequentlyQuestions(page: String): Single<FrequentlyQuestions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFrequentlyQuestions(frequentlyQuestions: FrequentlyQuestions)
}