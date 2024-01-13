package ir.mahdighanbarpour.khwarazmiapp.model.dataBase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.mahdighanbarpour.khwarazmiapp.model.data.FrequentlyQuestionsArray

class MainConverters {

    @TypeConverter
    fun fromFrequentlyQuestionsArray(value: String): List<FrequentlyQuestionsArray> {
        val listType = object : TypeToken<List<FrequentlyQuestionsArray>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromFrequentlyQuestionsArray(data: List<FrequentlyQuestionsArray>): String {
        val gson = Gson()
        return gson.toJson(data)
    }
}
