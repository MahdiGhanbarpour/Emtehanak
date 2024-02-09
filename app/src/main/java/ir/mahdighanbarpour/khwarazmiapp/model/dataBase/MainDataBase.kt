package ir.mahdighanbarpour.khwarazmiapp.model.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestion

// Database creation
@Database(version = 1, exportSchema = false, entities = [FrequentlyQuestion::class])
abstract class MainDataBase : RoomDatabase() {

    abstract val mainDao: FrequentlyQuestionsDao

    companion object {

        @Volatile
        private var dataBase: MainDataBase? = null

        fun getDataBase(context: Context): MainDataBase {
            synchronized(this) {
                if (dataBase == null) {
                    dataBase = Room.databaseBuilder(
                        context.applicationContext, MainDataBase::class.java, "mainDataBase.db"
                    ).allowMainThreadQueries().build()
                }
                return dataBase!!
            }
        }
    }
}