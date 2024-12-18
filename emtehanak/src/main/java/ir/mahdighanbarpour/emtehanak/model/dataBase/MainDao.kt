package ir.mahdighanbarpour.emtehanak.model.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ir.mahdighanbarpour.emtehanak.model.data.FrequentlyQuestion

@Dao
interface FrequentlyQuestionsDao {

    // Getting the data of the frequently asked questions of the posted page from the local database
    @Query("SELECT * FROM table_frequently_questions WHERE page = :page")
    fun getFrequentlyQuestions(page: String): Single<List<FrequentlyQuestion>>

    // Inserting the data of frequently asked questions of the posted page in the local database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFrequentlyQuestions(frequentlyQuestions: List<FrequentlyQuestion>)
}